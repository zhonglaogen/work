package com.rd.hcb.demo;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @author zlx
 * @date 2019-12-06 15:10
 */
public class TestExcel {

    public static void main(String[] args) throws IOException {

        OutputStream  os = null;
        File f = new File("C:\\Users\\RD\\Desktop\\test.xlsx");
        InputStream inputStream = new FileInputStream(f);
        Workbook xssfWorkbook = new XSSFWorkbook(inputStream);
//  XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0); //如果是.xlsx文件使用这个
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
            for (int col = 1; col < cols; col++) {
                r.createCell(col + 1).setCellValue("iii");
            }
        }
        os = new FileOutputStream(f.getAbsolutePath());
        xssfWorkbook.write(os);//最后一顶要写入输出流


    }
}
