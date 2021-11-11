package com.rd.hcb.demo.carddata;

import com.rd.hcb.demo.ExchangeTxtToExcel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * CardData
 *
 * @author zhonglx
 * @version 1.0.0 2021/9/8 12:37
 */
public class CardData {

    public static void main(String[] args) throws IOException {
        Map<Long, Set<String>> map = new HashMap<>();

        String oriFile = "D:\\tmpdata\\output.txt";
        FileReader fileReader = new FileReader(new File(oriFile));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String temp = null;
        //行号
        int rowNumber = 0;
        while ((temp = bufferedReader.readLine()) != null) {
            String[] split = temp.split(" ");
            Set<String> strings = map.computeIfAbsent(Long.valueOf(split[2]), k -> new HashSet<>());
            strings.add(split[1].split(",")[0]);
        }
        System.out.println("--------------------------------------");
        map.forEach((k, v) -> {
            System.out.println(k);
            for (String s : v) {
                System.out.print(s + "\t");
            }
            System.out.println();
        });
        System.out.println("--------------------------------------");
    }

    @Test
    public void test() throws IOException, InvalidFormatException {
        Map<Long, Set<String>> map = new HashMap<>();

        String oriFile = "D:\\tmpdata\\excel\\alldraw.txt";
        FileReader fileReader = new FileReader(new File(oriFile));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String temp = null;
        //行号
        int rowNumber = 0;
        while ((temp = bufferedReader.readLine()) != null) {
            if (StringUtils.isBlank(temp)) {
                continue;
            }
            String[] split = temp.split(" ");
            if (StringUtils.isBlank(split[2])) {
                continue;
            }
            Set<String> strings = map.computeIfAbsent(Long.valueOf(split[2]), k -> new HashSet<>());
            strings.add(split[1].split(",")[0]);
        }
        System.out.println("--------------------------------------");
        map.forEach((k, v) -> {
            System.out.println(k);
            for (String s : v) {
                System.out.print(s + "\t");
            }
            System.out.println();
        });
        System.out.println("--------------------------------------");




        String oriExcelFile = "D:\\tmpdata\\excel\\all.xlsx";
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
                long playerId = 0L;
                String dateStr = "";
                for (int col = 0; col < 2; col++) {
                    String value = r.getCell(col).getStringCellValue();
                    if (col == 1) {
                        if ("角色id".equals(value)) continue;
                        playerId = Long.valueOf(value);
                    } else if (col == 0) {
                        String[] s = value.split(" ");
                        if (s.length < 2) {
                            System.out.println();
                            continue;
                        }
                        dateStr = s[1];
                    }
                    System.out.printf("%10s", r.getCell(col).getStringCellValue());
                }
                if (map.containsKey(playerId)) {
                    if (map.get(playerId).contains(dateStr)) {
                        r.createCell(cols).setCellValue("抽卡");
                    }
                }
                System.out.println();
            }
        }

        FileOutputStream xlsStream = new FileOutputStream(xlsFile);
        workbook.write(xlsStream);
    }
}
