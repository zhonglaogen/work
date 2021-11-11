package com.rd.hcb.demo.carddata;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Dungeons
 *
 * @author zhonglx
 * @version 1.0.0 2021/9/9 11:03
 */
public class Dungeons {
    public static void main(String[] args) throws IOException {
        String oriExcelFile = "D:\\tmpdata\\player_dungeons.xlsx";
        File xlsFile = new File(oriExcelFile);
        // 获得工作簿
//        HSSFWorkbook workbook = (HSSFWorkbook) HssFWoWorkbookFactory.create(xlsFile);
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            // 获得行数
            int rows = sheet.getLastRowNum() + 1;
            // 获得列数，先获得一行，在得到改行列数
            Row tmp = sheet.getRow(0);
            if (tmp == null) {
                continue;
            }
            int cols = tmp.getPhysicalNumberOfCells();
            // 读取数据
            for (int row = 0; row < rows; row++) {
                Row r = sheet.getRow(row);
                for (int col = 0; col < cols - 1; col++) {
                    String value = r.getCell(col).getStringCellValue();
                    System.out.printf("%10s", value);
                }
            }
        }

    }
}
