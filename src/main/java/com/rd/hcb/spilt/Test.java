package com.rd.hcb.spilt;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * @author zlx
 * @date 2019-11-19 10:40
 */
public class Test {

    public static void main(String[] args) throws IOException {
        Document parse = Jsoup.parse(new File("C:\\Users\\RD\\Desktop"), "utf-8");
//        parse.getElementsByTag()

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


