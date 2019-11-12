package com.rd.hcb.task;

import java.io.*;
import java.nio.channels.FileChannel;

public class FuZhu {
    public static void main(String[] args) {
        String path="\\\\192.168.6.180\\hcb\\附注\\文档\\1.2主板、中小板";
        File files=new File(path);
        try {
            getFuZhuFileName(files);
        } catch (IOException e) {
            System.out.println("文件提取错误……");
            e.printStackTrace();
        }

    }

    private static void getFuZhuFileName(File file) throws IOException {
        for (File curfile:file.listFiles()){
            if (curfile.isDirectory()){
                System.out.println("########正在检索文件夹："+curfile.getName());
                getFuZhuFileName(curfile);
            }else {
                System.out.println("#########正在检查文件是否符合规则:"+curfile.getName());
                if (curfile.getName().contains("附注") && (curfile.getName().endsWith(".doc")||curfile.getName().endsWith(".docx"))){
                    System.out.println(curfile.getAbsolutePath()+"文件符合规则，正在进行文件拷贝……");

                    RandomAccessFile rw = new RandomAccessFile(curfile, "rw");
                    FileChannel channel = rw.getChannel();
                    FileOutputStream fo = new FileOutputStream("\\\\192.168.6.180\\hcb\\附注\\1、原始文件\\" + curfile.getName());
                    fo.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                    fo.close();
                }


            }

        }
    }


}
