package com.rd.hcb.spilt;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author zlx
 * @date 2019-11-19 10:40
 */
public class Test1 {


    /**
     *
     *  解决思路;表头为参考单元格错误的标准格式，将表头的标准单元格格式存入数组，
     *  判断一个tablecell是否有问题，在于他的父亲单元格的个数（这里参考系为表头），如果父亲个数大于1，则判断此单元格是错误的单元格
     *  判断前提表格必须是标准表格，不存在多一列或少一列的情况，否则会找不到父亲，直接返回错误字符串
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
        int currtData=gapArray[0];
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
                    td.attr("iserrorcell","true");
                    td.attr("style","background:red");
                }
                //处理完成后更新此行td的当前线段左端点
                leftPoint=rightPoint;

            }
        }
        return "Table is correct";
    }
}


