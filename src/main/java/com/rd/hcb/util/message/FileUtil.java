package com.rd.hcb.util.message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author zlx
 * @date 2019-12-20 14:45
 */
public class FileUtil {

    public static void writeToFileEnd(String content, String fileName) throws IOException {
        RandomAccessFile rw = new RandomAccessFile(fileName, "rw");
        System.out.println(fileName);

        // 文件长度，字节数
        long fileLength = rw.length();
        //将写文件指针移到文件尾。
        rw.seek(fileLength);
        rw.write(content.getBytes());
        rw.close();
    }

    public static void writeToFileCover(String content, String fileName) throws IOException {
        RandomAccessFile rw = new RandomAccessFile(fileName, "rw");
        rw.write(content.getBytes());
        rw.close();
    }
}
