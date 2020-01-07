package com.rd.hcb.demo.work;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author zlx
 * @date 2019-12-27 09:53
 */
public class HtmlChangeTxt {
    @Test
    public void test() throws IOException {
        File file = new File("C:\\Users\\RD\\Desktop\\转换txt\\html\\");
        String output = "C:\\Users\\RD\\Desktop\\转换txt\\txt\\";
        for (File file1: file.listFiles()){
            Document parse = Jsoup.parse(file1, "utf-8");
            RandomAccessFile rw = new RandomAccessFile(new File(output+file1.getName() + ".txt"),"rw");
            change(parse,rw);
            rw.close();
        }
    }

    public void change(Document doc, RandomAccessFile rw) throws IOException {
        String text = doc.body().text();
        rw.write(text.getBytes());
    }
}
