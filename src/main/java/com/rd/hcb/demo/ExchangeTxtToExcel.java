package com.rd.hcb.demo;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.model.FibBase;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zlx
 * @date 2019-11-07 17:34
 */
public class ExchangeTxtToExcel {
    public static void main(String[] args) throws IOException {
//        test1();
        String oriFile = "C:\\Users\\RD\\Desktop\\output.txt";
        String toExcel = "C:\\Users\\RD\\Desktop\\table.xlsx";
        bioReadToExcel(oriFile,toExcel);
//        test1();
        return;
    }

    @Test
    public void changeExcel() throws IOException {
        File file = new File("D:\\招股书1\\test2\\excel");
        for (File file1 : file.listFiles()){
//            int i = file1.getName().lastIndexOf("\\");
//            file.getName().substring(0,i)
            bioReadToExcel(file1.getAbsolutePath(),file1.getAbsolutePath()+".xlsx");
        }
    }

    @Test
    public void delete(){
        File file = new File("D:\\招股书1\\test2\\excel");
        for (File file1 : file.listFiles()){
            if (file1.getName().endsWith("txt")){
                file1.delete();
            }
        }



    }

    /**
     * bio按行读文件
     *
     * @throws IOException
     */
    private static void bioRead() throws IOException {
        FileReader fileReader = new FileReader(new File("C:\\Users\\RD\\Desktop\\test.txt"));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String temp = null;
        while ((temp = bufferedReader.readLine()) != null) {
            String[] split = temp.split("@");
            for (int i = 0; i < split.length; i++) {
                System.out.print(split[i] + "--");
            }
            System.out.println();
        }
        bufferedReader.close();
    }


    /**
     * text 转换为 excel
     *
     * @param oriFile
     * @param toExcel
     * @throws IOException
     */
    private static void bioReadToExcel(String oriFile, String toExcel) throws IOException {
        // 创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表
        XSSFSheet sheet = workbook.createSheet("sheet1");

        FileReader fileReader = new FileReader(new File(oriFile));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String temp = null;
        //行号
        int rowNumber = 0;
        while ((temp = bufferedReader.readLine()) != null) {
            String[] split = temp.split("@");
            XSSFRow row = sheet.createRow(rowNumber);
            //写入一行数据
            int length = split.length;
            for (int i = 0; i < length; i++) {
                row.createCell(i).setCellValue(split[i]);
                System.out.print(split[i] + "\t");
            }
            rowNumber++;
            System.out.println();
        }
//        将所有数据写入文件中
        File xlsFile = new File(toExcel);
        FileOutputStream xlsStream = new FileOutputStream(xlsFile);
        workbook.write(xlsStream);

        bufferedReader.close();
    }


    private static void niotest1() {
        try {
            FileReader fileReader = new FileReader(new File("C:\\Users\\RD\\Desktop\\test.xlsx"));
            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\RD\\Desktop\\tt.txt"));
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            int read = channel.read(allocate);
            StringBuilder stringBuilder = new StringBuilder();
            while (read != -1) {
                //读缓冲区模式
                allocate.flip();

                System.out.println();
                //切换为写模式
                allocate.compact();
                read = channel.read(allocate);
            }
//            System.out.println(stringBuilder.toString());
            channel.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
