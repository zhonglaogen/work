package com.rd.hcb.demo.work;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.util.Map;

/**
 * @author zlx
 * @date 2019-12-06 15:10
 */
public class TestExcel {


    /**
     * 更新excel的值
     *
     * @param f
     * @param jsonObject
     * @throws IOException
     */
    public static void updateExcel(File f, JSONObject jsonObject) throws IOException {
        OutputStream os = null;
        InputStream inputStream = new FileInputStream(f);
        Workbook xssfWorkbook = new XSSFWorkbook(inputStream);
        Sheet sheet = xssfWorkbook.getSheetAt(0);
        // 获得行数
        int rows = sheet.getLastRowNum() + 1;
        // 获得列数，先获得一行，在得到改行列数
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            return;
        }
        int cols = tmp.getPhysicalNumberOfCells();
        // 读取数据
        for (int row = 0; row < rows; row++) {
            Row r = sheet.getRow(row);
            for (int col = 1; col < 2; col++) {
                String stringCellValue = r.getCell(col).getStringCellValue();
                for (Map.Entry<String, Object> en : jsonObject.entrySet()) {
                    //数据相同，添加第三列
                    if (en.getKey().equals(stringCellValue)) {
                        r.createCell(col + 1).setCellValue(en.getValue().toString());
                    }
                }
            }
        }
        os = new FileOutputStream(f.getAbsolutePath());
        xssfWorkbook.write(os);//最后一顶要写入输出流
    }

    /**
     * 先读取在更改
     *
     * @param f
     * @throws IOException
     */
    public static void updateExcel2(File f) throws IOException {
        OutputStream os = null;
        InputStream inputStream = new FileInputStream(f);
        Workbook xssfWorkbook = new XSSFWorkbook(inputStream);
        Sheet sheet = xssfWorkbook.getSheetAt(0);
        // 获得行数
        int rows = sheet.getLastRowNum() + 1;
        // 获得列数，先获得一行，在得到改行列数
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            return;
        }
        int cols = tmp.getPhysicalNumberOfCells();
        // 读取数据
        for (int row = 0; row < rows; row++) {
            Row r = sheet.getRow(row);
            String stringCellValue = r.getCell(0).getStringCellValue();
            //此处判断值是否相等
            r.getCell(0).setCellValue("dsa");
        }
        os = new FileOutputStream(f.getAbsolutePath());
        xssfWorkbook.write(os);//最后一顶要写入输出流
    }

    @Test
    public void test() throws IOException {
        updateExcel2(new File("C:\\Users\\RD\\Desktop\\oo.xlsx"));
    }

}
