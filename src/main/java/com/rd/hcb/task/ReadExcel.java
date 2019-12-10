package com.rd.hcb.task;

import com.rd.hcb.task.entity.ConfigFile;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zlx
 * @date 2019-11-29 15:31
 */
public class ReadExcel {
    /**
     * 插入nlp的数据
     *
     * @param path
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public List<String> readExcel(String path) throws IOException, InvalidFormatException {

        List<String> ls = new ArrayList<>();
        File xlsFile = new File(path);
        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(xlsFile);
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        Sheet sheet = workbook.getSheetAt(1);
        // 获得行数
        int rows = sheet.getLastRowNum() + 1;
        // 获得列数，先获得一行，在得到改行列数
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            return ls;
        }
        // 读取数据
        for (int row = 0; row < rows; row++) {
            if ((row + 1) % 3 == 0) {
                Row r = sheet.getRow(row);
                if (r.getCell(1) != null) {
                    System.out.printf("%10s", r.getCell(1).getStringCellValue());
                    ls.add(r.getCell(1).getStringCellValue());
                }
                System.out.println();
            }

        }
        return ls;
    }

    @Test
    public void printExcel() throws IOException, InvalidFormatException {
        List<String> datas = readExcel("C:\\Users\\RD\\Desktop\\配置文件（沪深&三板年报&半年报通用）1128.xlsx");
        StringBuilder insertSql = new StringBuilder("insert into finance_nlp_table_type(table_nlp_type,financing_type,del_falg) values");
        String financing_type = "347";
        for (int i = 0; i < datas.size(); i++) {
            if (i != 0) {
                insertSql.append(",('" + datas.get(i) + "','"+financing_type+"','0')");
            } else {
                insertSql.append("('" + datas.get(i) + "','"+financing_type+"','0')");
            }

        }
        System.out.println(insertSql.toString());
        System.out.println(datas.size());

    }

    /**
     * 解析excel 的数据
     * @param path
     * @param sheetPage seet页数
     * @param financing_type 当前finance_ype    346沪深 347新三板
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */

    public List<ConfigFile> getConfigs(String path, int sheetPage, String financing_type) throws IOException, InvalidFormatException {
        List<ConfigFile> configs = new ArrayList<>();
        File xlsFile = new File(path);
        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(xlsFile);
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        Sheet sheet = workbook.getSheetAt(sheetPage);
        // 获得行数
        int rows = sheet.getLastRowNum() + 1;
        // 获得列数，先获得一行，在得到改行列数
        Row tmp = sheet.getRow(0);
        if (tmp == null) {
            return configs;
        }
        // 读取数据
        for (int row = 0; row < rows; row++) {
            if ((row + 1) % 3 == 0) {
                long timeStap = System.currentTimeMillis();
                //防止重复加上随机数
                String relate_id = String.valueOf(timeStap) + row;
                Row r = sheet.getRow(row);
                if (r.getCell(0) != null) {
                    String stringCellValue = r.getCell(0).getStringCellValue();
                    String[] split = stringCellValue.split("/");
                    for (int i = 0; i < split.length; i++) {
                        ConfigFile configFile = new ConfigFile();
                        String moudle = null;
                        switch (split[i]) {
                            case "新三板年报":
                                moudle = "6";
                                break;
                            case "半年报":
                                moudle = "7";
                                break;
                            case "审计报告":
                                moudle = "4";
                                break;
                            case "附注":
                                moudle = "3";
                                break;
                            default:
                                break;
                        }
                        configFile.setMoudle(moudle);
                        configFile.setFinancing_type(financing_type);
                        configFile.setTable_nlp_type(r.getCell(1).getStringCellValue());
                        configFile.setRelate_id(relate_id);
                        configs.add(configFile);
                    }
                }
                Row row2 = sheet.getRow(row + 1);
                if (row2.getCell(0) != null) {
                    String stringCellValue = row2.getCell(0).getStringCellValue();
                    String[] split = stringCellValue.split("/");
                    for (int i = 0; i < split.length; i++) {
                        ConfigFile configFile = new ConfigFile();
                        String moudle = null;
                        switch (split[i]) {
                            case "新三板年报":
                                moudle = "6";
                                break;
                            case "半年报":
                                moudle = "7";
                                break;
                            case "审计报告":
                                moudle = "4";
                                break;
                            case "附注":
                                moudle = "3";
                                break;
                            default:
                                break;
                        }
                        configFile.setMoudle(moudle);
                        configFile.setFinancing_type(financing_type);
                        configFile.setTable_nlp_type(r.getCell(1).getStringCellValue());
                        configFile.setRelate_id(relate_id);
                        configs.add(configFile);
                    }

                }
            }

        }
        return configs;
    }


    /**
     * 获取数据库连接
     * @return
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            //初始化驱动类com.mysql.jdbc.Driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://rm-2ze7bn7h9mk41f7c7yo.mysql.rds.aliyuncs.com:3306/rd_check_uat?characterEncoding=UTF-8", "cloudproduct", "RongdaCP518%!*");
            //该类就在 mysql-connector-java-5.0.8-bin.jar中,如果忘记了第一个步骤的导包，就会抛出ClassNotFoundException
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 构建sql语句
     * @param configs
     * @return
     */
    public String constructSql(List<ConfigFile> configs) {
        StringBuilder sb = new StringBuilder("insert into finance_nlp_config(financing_type,moudle,nlp_id,relate_id) values");
        for (int i = 0; i < configs.size(); i++) {
            ConfigFile configFile = configs.get(i);
            String curSql;
            if (i != 0) {
                curSql = ",('" + configFile.getFinancing_type() + "','"
                        + configFile.getMoudle()
                        + "'," + configFile.getNlp_id()
                        + ",'" + configFile.getRelate_id() + "')";
                sb.append(curSql);
            } else {
                curSql = "('" + configFile.getFinancing_type() + "','"
                        + configFile.getMoudle()
                        + "'," + configFile.getNlp_id()
                        + ",'" + configFile.getRelate_id() + "')";
                sb.append(curSql);
            }
        }
        return sb.toString();
    }


    public String getRandom() {
        int max = 99, min = 10;
        int ran2 = (int) (Math.random() * (max - min) + min);
        return String.valueOf(ran2);
    }

    @Test
    public void getConfig() throws IOException, InvalidFormatException {
        //!!!!!!需要修改代码中的第一行第一列分隔符前面的数据，用来做匹配
        List<ConfigFile> configs =
                getConfigs("C:\\Users\\RD\\Desktop\\配置文件（沪深&三板年报&半年报通用）1128.xlsx", 1,"347");
        System.out.println(configs);
        getFullMessage(configs);
        System.out.println(configs.size());
        System.out.println(configs);
        String s = constructSql(configs);
        System.out.println(s);

    }

    /**
     * 获取关联nlp的id
     * @param configs
     */
    private void getFullMessage(List<ConfigFile> configs) {
        Connection connection = getConnection();
        configs.forEach(l1 -> {
            //查询此nlptype的关联id,relate_id
            String table_nlp_type = l1.getTable_nlp_type();
            String sql = "select id from finance_nlp_table_type where table_nlp_type = ? and financing_type = ? ";
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, table_nlp_type);
                preparedStatement.setString(2, l1.getFinancing_type());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                l1.setNlp_id(String.valueOf(resultSet.getInt("id")));
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        try {
            connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public ReadExcel(){
        System.out.println("construct");
    }



}
