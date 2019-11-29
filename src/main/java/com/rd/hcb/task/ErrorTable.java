package com.rd.hcb.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author zlx
 * @date 2019-11-22 15:48
 */
public class ErrorTable {



    /**
     * 解决思路;表头为参考单元格错误的标准格式，将表头的标准单元格格式存入数组，
     * 判断一个tablecell是否有问题，在于他的父亲单元格的个数（这里参考系为表头），如果父亲个数大于1，则判断此单元格是错误的单元格
     * 判断前提表格必须是标准表格，不存在多一列或少一列的情况，否则会找不到父亲，直接返回错误字符串
     *
     * @throws IOException
     */


    @Test
    public void testErrorTableCell() throws IOException {
        Document parse = Jsoup.parse(new File("D:\\myhtml\\isformat.html"), "gbk");
        Element table = parse.body().select("table").get(0);
        String s = CheckTableCell(table);
        System.out.println(s);
        System.out.println(parse.html());
    }

    /**
     * 标记错误的tableCell
     *
     * @param table
     */
    private String CheckTableCell(Element table) {
        Elements trs = table.getElementsByTag("tr");
        int trSize = trs.size();

        //初始化表头作为标准的参考
        Element element = trs.get(0);
        Elements firstTds = element.getElementsByTag("td");
        //第一列的单元格数目
        int firstTdSize = firstTds.size();
        //构建距离线段数组，来存每一个单元格间距线段
        int[] gapArray = new int[firstTdSize + 1];
        int currtData = gapArray[0];
        for (int i = 0; i < firstTdSize; i++) {
            //获取跨列数目
            String colspan = firstTds.get(i).attr("colspan");
            boolean isOneCol = colspan == null || colspan.equals("") || colspan.equals("0");
            if (isOneCol) {
                colspan = "1";
            }
            gapArray[i + 1] = currtData + Integer.parseInt(colspan);
            currtData = gapArray[i + 1];
        }

//        for (int i = 0; i < gapArray.length; i++) {
//            System.out.println(gapArray[i]);
//        }

        for (int i = 1; i < trSize; i++) {
            Elements tds = trs.get(i).getElementsByTag("td");

            int leftPoint = 0;

            for (int j = 0; j < tds.size(); j++) {
                Element td = tds.get(j);
                String colspan = td.attr("colspan");
                boolean isOneCol = colspan == null || colspan.equals("") || colspan.equals("0");
                if (isOneCol) {
                    colspan = "1";
                }
                int offect = Integer.parseInt(colspan);
                //当前cell的右边界点
                int rightPoint = leftPoint + offect;
                //每一个cell都具有线段的left点和right点
                //寻找左边界点对应在gapArray的位置
                int leftIndex = 0;
                int rightIndex = gapArray.length - 1;
                while (leftPoint > gapArray[leftIndex]) {
                    leftIndex++;
                    //表格不规范，cell超出标准表头的长度
                    if (leftIndex > gapArray.length - 1) {
                        return "Table is error";
                    }
                }
                if (gapArray[leftIndex] != leftPoint) {
                    leftIndex--;
                }
                while (rightPoint < gapArray[rightIndex]) {
                    rightIndex--;
                    if (rightIndex < 0) {
                        return "Table is error";
                    }
                }
                if (gapArray[rightIndex] != rightPoint) {
                    rightIndex++;
                }
                int parentCounts = rightIndex - leftIndex;
                if (parentCounts > 1) {
                    //进行错误单元格的标记
                    td.attr("iserrorcell", "true");
                    td.attr("style", "background:red");
                }
                //处理完成后更新此行td的当前线段左端点
                leftPoint = rightPoint;

            }
        }
        return "Table is correct";
    }





    @Test
    public void testError() throws IOException {
        System.out.println(new File("D:\\myhtml\\test3.html").exists());
        Document parse = Jsoup.parse(new File("D:\\myhtml\\test.html"), "utf-8");
        Element body = parse.body();
//        System.out.println(body.text());
        Elements tables = body.select("table");
        Element table = tables.get(0);

        Element element = tableBreaking(table);
        System.out.println(element);

    }

    public static Element tableBreaking(Element table) {
        try {
            int trindex = 0;
            int tdindex = 0;
            int offset;
            Elements trs = table.select("tr");
            //遍历该表格内的所有的<tr> <tr/>
            //#######################alter################################
            //由于跨行处理是向下一行的offset位置插入某一列，所以下一行的行应该是已经规则化的(规则化列的)，这样才能正确插入跨行的某个单元格的正确位置
            //采用逆序处理可以解决
            for (trindex = trs.size() - 1; trindex >= 0; trindex--) {
                offset = 0;
                // 获取一个tr
                Element tr = trs.get(trindex);
                // 获取该行的所有td节点
//                    Elements tds = tr.select("td");
                // 选择某一个td节点
                Elements tds = tr.children();
                int tdSize = tds.size();
                for (tdindex = 0; tdindex < tdSize; tdindex++) {
                    Element td = tds.get(tdindex);
                    // 如果跨列了
                    String tdcolspan = td.attr("colspan");

                    //不存在跨列为0
                    tdcolspan = tdcolspan.length() <= 0 ? "0" : tdcolspan;
                    // 针对 跨列进行处理，将rowspan消化。在下行增加
                    int tdcolspannumber = Integer.parseInt(tdcolspan);
                    if (tdcolspannumber > 1) {
                        td.removeAttr("colspan");
                        Element baseTdEle = null;

                        // 跨列了只需要在原基础上追加单元格就行。游标从1开始，除去本身
                        for (int tempindex = 1; tempindex < tdcolspannumber; tempindex++) {
                            baseTdEle = td.clone();
                            //放在后面
                            td.after(baseTdEle);

                            //################alter############################
                            //添加跨列后，当前列总数不变，处理跨列后应该及时更新当前的所有列的总数，否则对比会发生错误
                            tds = tr.getElementsByTag("td");
                            tdSize = tds.size();


                        }
                    }
                    // 如果跨行了，需要占领行的知切割成多行，并保留colspan属性
                    String tdrowspan = td.attr("rowspan");
                    tdrowspan = tdrowspan.length() <= 0 ? "0" : tdrowspan;
                    // 针对 跨行进行处理，将rowspan消化。在下行增加
                    int tdrowspannumber = Integer.parseInt(tdrowspan);


                    //添加跨行的某列数据
                    if (tdrowspannumber > 1) {
                        td.removeAttr("rowspan");
                        Element baseTdEle;
                        int startRow = 0;
                        int startTd = 0;

                        for (int tempindex = 1; tempindex < tdrowspannumber; tempindex++) {
                            // 从哪一行开始，如果是rowspan本身自己不需要再进行扩展，只要其他的扩展就行了
                            startRow = trindex + tempindex;
                            // 从哪一列开始，从自己开始。
//                                startTd = tdindex + 1;

                            // 初始化一个basetd，用于复制到其他使用。
                            //#######################alter############################
                            baseTdEle=td.clone();

                            if (startRow >= trs.size()) {
                                break;
                            }
                            // 插入一个basetd，到需要扩展的行中去。
                            Element next = trs.get(startRow);


                            //可插入的前提是当前列已经规则化
                            //直接插入insertChildren，会插入错误的位置，debug时发现当前行下的子元素不止td元素
                            //要获取当前行下的列
                            //#######################alter############################
                            Elements tds1 = next.getElementsByTag("td");
                            if (offset  < tds1.size()) {
                                //会有不是td的child，不能这样直接插入

                                Element element = tds1.get(offset);
                                //插入的是当前列的前一列，让插入列位置变为offset
                                element.before(baseTdEle);

                                //####################before#############################
//                                next.insertChildren(offset, baseTdEle);
                            } else {
//                                next.appendChild(baseTdEle);
                                tds1.get(tds1.size()-1).after(baseTdEle);
                            }

                        }
                    }

                    //########################before#########################

                    //每列逻辑处理完成
//                    offset += tdcolspannumber == 0 ? 1 : tdcolspannumber;

                    //######################after###########################
                    //每次处理完一行元素就+1，表示当前处理行的位置，其实就相当于tdindex
                    offset += 1;


                    //########################end#########################


                    //########################delete#########################
                    //同时跨行 跨列的情况，处理完当前行后，加上tdcolspannumber会跳过下一列的跨行操作，所以重头遍历
//                    if (tdrowspannumber > 1 && tdcolspannumber > 1) {
//                        tdindex = 0;
//                        tdSize = tr.children().size();
//                        tds = tr.children();
//                        continue;
//                    }
                    //########################delete#########################

                }
            }
            return table;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
