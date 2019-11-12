package com.rd.hcb.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

/**
 * 附注文件没有level,排查一级目录没有level的问题
 *
 * @author zlx
 * @date 2019-11-09 15:07
 */
public class FuZhuHeCha {
    public static void main(String[] args) throws IOException {
        File files = new File("C:\\Users\\RD\\Desktop\\test\\error");
        File[] files1 = files.listFiles();
        for (File file : files1) {
            if (file.isFile()) {
                parseHtmlLevel(file);
            } else {
                if (file.listFiles()[0].isFile() && file.listFiles()[0].getName().endsWith(".html")) {
                    parseHtmlLevel(file.listFiles()[0]);
                }
            }

///
        }
//        parseHtmlLevel(new File("\\\\192.168.6.180\\hcb\\招股说明书\\主板\\3、目录html\\1-1 万达杰创业板IPO招股说明书-0607-清洁版.html"));

    }

    private static void parseHtmlLevel(File file) throws IOException {
        System.out.println("检索文件……" + file.getName());
        Document parse = Jsoup.parse(file, "UTF-8");
        Element body = parse.body();
        Elements p = body.getElementsByTag("p");
        int size = p.size();
        for (int i = 0; i < size; i++) {
            Element element = p.get(i);
            String iscatalog = element.attr("iscatalog");
            if (iscatalog != null && !iscatalog.equals("") && iscatalog.equals("true")) {
                String level = element.attr("level");
//                    System.out.println(element.text());
                if (level == null || level.equals("")) {
                    System.out.println("**********************" + file.getName());
//                    System.out.println(element.text());
                    break;
                }
            }

        }
    }
}
