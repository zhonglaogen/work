package com.rd.hcb.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import static sun.plugin2.os.windows.FLASHWINFO.size;

public class CompanyName2 {


    public static void main(String[] args) {
        File excelFile = new File("D:\\CompanyName.txt");
        RandomAccessFile w = null;
        FileChannel channel;
        try {
            //需要写的位置
            w = new RandomAccessFile(excelFile, "rw");
            File dir = new File("D:\\dealfile");
            File[] files = dir.listFiles();
            int length = files.length;
            for (int i = 0; i < length; i++) {
                getHtmlCompanyName2(files[i], w);
            }
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
        System.out.println("文件是否存在" + htmlFile.exists());
        System.out.println("正在解析文件" + htmlFile.toString());

        Document parse = Jsoup.parse(htmlFile, "utf-8");
        Element body = parse.body();
        //获取istitle,并且为一级目录
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
                                if (tds.size() != 2) {
                                    for (int k = 0; k < tds.size(); k++) {
                                        if (k != 1) {
                                            //输出当前列
                                            System.out.print(tds.get(k).text() + "----------");
                                            String block = tds.get(k).text() + "@@";
//                                            w.write(block.getBytes());

                                        }
                                    }
                                    //每行添加一个换行
                                    System.out.println();
//                                    w.write("\n".getBytes());
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


    /**
     * @param htmlFile
     * @param w
     * @throws IOException
     */
    private static void getHtmlCompanyName2(File htmlFile, RandomAccessFile w) throws IOException {
        System.out.println("文件是否存在" + htmlFile.exists());
        System.out.println("正在解析文件" + htmlFile.toString());

        //是否包含公司
        boolean isContainCompany = false;
        //是否包含释义
        boolean isCatalogExplain = false;
        //包含五个指
        boolean isFiveZhi = false;
        //列数异常
        boolean tdCountError = false;


        Document parse = Jsoup.parse(htmlFile, "utf-8");
        Element body = parse.body();
        //获取istitle,并且为一级目录
        Elements istitles = body.getElementsByAttribute("istitle");
        System.out.println("目录数量：" + istitles.size());
        //遍历目录
        for (Element istitle : istitles) {
            //包含释义
            boolean shiyi = istitle.text().contains("释 义") || istitle.text().contains("释义");
            if (shiyi) {
                //包含释义
                isCatalogExplain = true;
                System.out.println(istitle.text());
                Element table = istitle.nextElementSibling();
                //只遍历前20个表格
                int findTableCount = 0;

                while (true) {
                    if (table.tag().getName().equals("table")) {
                        //包含5个指
                        String st = table.text();
                        int count = (st.length() - st.replace("指", "").length()) / "指".length();
                        System.out.println("指的个数" + count);
                        if (count > 5) {
                            isFiveZhi = true;
                            Element tbody = table.select("tbody").first();

                            Elements trs = tbody.getElementsByTag("tr");
                            for (Element tr : trs) {
                                //遍历包含公司的每一行
                                if (tr.text().contains("公司")) {
                                    //包含公司，则此文件不用复制出来
                                    isContainCompany = true;
                                    //判断列是否为三列，若果是，去掉中间列
                                    Elements tds = tr.getElementsByTag("td");
                                    int tdsSize = tds.size();
                                    if (tdsSize == 3) {
                                        for (int i = 0; i < tdsSize; i++) {
                                            if (i != 1) {
                                                String tdText = tds.get(i).text() + "@@";
//                                                w.write(tdText.getBytes());
                                                System.out.print(tds.get(i).text() + "--------");
                                            }
                                        }
                                        //此行的所有列打印完毕，换行
//                                        w.write("\n".getBytes());
                                        System.out.println();
                                    } else if (tdsSize == 2) {
                                        for (int i = 0; i < tdsSize; i++) {
                                            String tdText = tds.get(i).text() + "@@";
//                                            w.write(tdText.getBytes());
                                            System.out.print(tds.get(i).text() + "--------");
                                        }
                                        //此行的所有列打印完毕，换行
//                                        w.write("\n".getBytes());
                                        System.out.println();
                                    } else {
                                        tdCountError = true;
                                    }


                                }

                            }

                        }
                    }
                    table = table.nextElementSibling();
                    if (table== null || table.attr("class") == null || table.attr("class").contains("onecatalog")) {
                        System.out.println("到下一层一级目录不包含‘指’");
                        break;
                    }
                }
                //已经找到释义标题
                break;
            }

        }
        //判断未读取出的原因
        if (!isCatalogExplain || !isFiveZhi || !isContainCompany || tdCountError) {
            System.out.println(htmlFile.getName() + "#是否存在释义" + isCatalogExplain);
            System.out.println(htmlFile.getName() + "#是否含有5个指" + isFiveZhi);
            System.out.println(htmlFile.getName() + "#是否包含公司" + isContainCompany);
            System.out.println(htmlFile.getName() + "#是否列数目异常" + tdCountError);
            System.out.println(htmlFile.getName() + "#文件解析异常#####################");
            //拷贝解析异常文件
            RandomAccessFile rw = new RandomAccessFile(htmlFile, "rw");
            FileChannel channel = rw.getChannel();
            FileOutputStream fo = new FileOutputStream("D:\\error\\" + htmlFile.getName());
            fo.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
            fo.close();


        }


    }


}

