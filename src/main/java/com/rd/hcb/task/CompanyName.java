package com.rd.hcb.task;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CompanyName {


    public static void main(String[] args) {
        File excelFile = new File("D:\\CompanyName.txt");
        RandomAccessFile w = null;
        FileChannel channel;
        try {
            //需要写的位置
            w = new RandomAccessFile(excelFile, "rw");
            getCompanyName(w);
        } catch (IOException e) {
            System.out.println("html 内容提取 异常");
            e.printStackTrace();
        } finally {
            try {
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        File file = new File("\\\\192.168.6.180\\hcb\\招股说明书");


//        try {
//            getHtmlCompanyName(new File("\\\\192.168.6.180\\hcb\\招股说明书\\主板\\3、目录html\\1-1 招股说明书（申报稿） (2).html"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    private static void getCompanyName(RandomAccessFile w) throws IOException {
        //检查路径是否存在
        File file = new File("\\\\192.168.6.180\\hcb\\招股说明书");
        System.out.println("文件是否存在" + file.exists());
        //获取子路径,拿到每个版块
        File[] platesDir = file.listFiles();
        //遍历版块
        for (File plate : platesDir) {

            File[] plateDir = plate.listFiles();
            //获取目录为“3、目录html”
            //存在.DS_Store文件，文件无子文件
            if (plateDir != null) {
                for (File dirHtmlPath : plateDir) {

                    if (dirHtmlPath.toString().contains("目录html")) {
                        File[] catalogList = dirHtmlPath.listFiles();
                        //遍历目录html文件夹，拿到其中的文件
                        //存在.DS_Store文件，文件无子文件
                        if (catalogList != null) {
                            for (File dirHtmlFile : catalogList) {

                                File htmlFile = getHtmlFile(dirHtmlFile);
                                //有可能文件夹下面不存在文件，获取文件为.DS_Store舍去
                                if (htmlFile != null && htmlFile.toString().contains(".html")) {
                                    //进行html内容的提取
                                    getHtmlCompanyName(htmlFile, w);
                                }

                            }
                        }

                    }
                }

            }

        }
    }

    /**
     * 获取文件
     */
    private static File getHtmlFile(File filePath) {
        //判断是否为文件
        if (!filePath.isDirectory()) {
            return filePath;
        }
        //寻找此目录的下一级
        File[] deepPath = filePath.listFiles();
        if (deepPath != null) {
            getHtmlFile(deepPath[0]);
        }
        //目录下缺失文件
        return null;


    }

    /**
     * 获取公司名称
     */
    private static void getHtmlCompanyName(File htmlFile, RandomAccessFile w) throws IOException {
        System.out.println("文件是否存在"+htmlFile.exists());
        System.out.println("正在解析文件"+htmlFile.toString());

        Document parse = Jsoup.parse(htmlFile, "utf-8");
        Element body = parse.body();
        //class为MsoNormal,iscatalog为true，mso-outline-level：为1   clearcatalogtitle
        //linetag为h1,iscatalog=true    clearcatalogtitle
        Element select = body.getElementById("div_content");
        Elements elementsByAttributeValue = select.getAllElements().select("[iscatalog=true]");
        int size = elementsByAttributeValue.size();
        for (int i = 0; i < size; i++) {
            if (elementsByAttributeValue.get(i).text().contains("释义")) {
//                System.out.println(elementsByAttributeValue.get(i).text());
                Element element = elementsByAttributeValue.get(i);
                //获取所有表格
                //判断是否到达下一个一级目录：---是表格
                Element tableTag = element.nextElementSibling();
                while (true) {
                    if (tableTag.tag().toString().equals("table")) {
                        //解析表格
                        Elements trs = tableTag.getElementsByTag("tr");
                        System.out.println("行数" + trs.size());
                        for (int j = 0; j < trs.size(); j++) {
                            if (trs.get(j).text().contains("公司")) {
                                Elements tds = trs.get(j).getElementsByTag("td");
                                //判断是两列还是三列
                                if (tds.size() == 3) {
                                    for (int k = 0; k < tds.size(); k++) {
                                        if (k != 1) {
                                            //输出当前列
                                            System.out.print(tds.get(k).text() + "----------");
                                            String block = tds.get(k).text() + "@@";
                                            w.write(block.getBytes());

                                        }
                                    }
                                    //每行添加一个换行
                                    System.out.println();
                                    w.write("\n".getBytes());
                                }
                            }
                        }
                        break;
                    }
                    //此元素不是表格，跳过
                    tableTag = tableTag.nextElementSibling();

                }
            }

        }

    }

}

