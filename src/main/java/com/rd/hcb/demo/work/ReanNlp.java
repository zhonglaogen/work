package com.rd.hcb.demo.work;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author zlx
 * @date 2019-12-05 16:03
 */
public class ReanNlp {
    @Test
    public void eachFile() {
        File f = new File("D:\\招股书1\\test2\\excel");
        for (File fil : f.listFiles()) {
            try {
                test(fil);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testt() throws IOException, InvalidFormatException {
        test(new File("D:\\招股书1\\主板\\excel\\" +
                "[荣大二郎神]青岛森麒麟轮胎股份有限公司首次公开发行股票招股说明书（申报稿2019年11月11日报送）_9.docx.html.txt.xlsx"));
    }


    public void test(File xlsFile) throws IOException, InvalidFormatException {
        StringBuilder resSb = new StringBuilder();

        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(xlsFile);
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
                for (int col = 1; col < cols; col++) {
                    String stringCellValue = r.getCell(col).getStringCellValue();
                    resSb.append(stringCellValue);
//                    System.out.println(stringCellValue);
                }
                resSb.append("*|*");
            }
        }
        System.out.println(resSb.toString());
        JSONObject jsonObject = post2(resSb.toString());
        //找回此文本在html的位置
        System.out.println("******************找回此文本在html的位置");
        markTable(jsonObject, xlsFile);
        System.out.println("******************找回此文本在excel的位置");
        writeExcel(xlsFile, jsonObject);


    }
    private void markTable(JSONObject jsonObject, File xlsFile) throws IOException {
        //寻找html
        int length = xlsFile.getName().length();
        String htmlName = xlsFile.getName().substring(0, length - 8);
        markParse(new File("D:\\招股书1\\test2\\html\\" + htmlName), jsonObject);
    }

    /**
     * 修改文件
     *
     * @param xlsFile
     * @param jsonObject
     * @throws IOException
     */
    private void writeExcel(File xlsFile, JSONObject jsonObject) throws IOException {
        TestExcel.updateExcel(xlsFile, jsonObject);
    }




    @Test
    public void test11() throws IOException {
//        for (int i = 0; i < 10; i++) {
            JSONObject jsonObject = post2("财务会计信息与管理层分析发行人财务状况分析所有者权益变动分析盈余公积*|*" +
                    "财务会计信息与管理层分析发行人财务状况分析所有者权益变动分析资本公积*|*"+
                    "财务会计信息与管理层分析发行人财务状况分析所有者权益变动分析所有者权益变动情况*|*");
            System.out.println(jsonObject.toString());
//        }

    }

    public JSONObject post2(String text) throws IOException {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("table_path", text));
//        formparams.add(new BasicNameValuePair("password", ""));
        HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)//一、连接超时：connectionTimeout-->指的是连接一个url的连接等待时间
                .setSocketTimeout(5000)// 二、读取数据超时：SocketTimeout-->指的是连接上一个url，获取response的返回等待时间
                .setConnectionRequestTimeout(5000)
                .build();

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://192.168.6.79:8011/caiwu_table_path");
        post.setEntity(reqEntity);
        post.setConfig(requestConfig);
        HttpResponse response = client.execute(post);

        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            String message = EntityUtils.toString(resEntity, "utf-8");
//            EntityUtils.toString(response.getEntity(), "GBK");
            System.out.println();
            JSONObject jsonObject = JSONObject.parseObject(message);
//            System.out.println(jsonObject.toString());
            jsonObject.forEach((k, v) -> {
                //寻找k，对应的表格的
                System.out.println(k + "\t" + v);
            });
            return jsonObject;

        } else {
            System.out.println("请求失败");
            return null;
        }


    }


    static List<String> standWordList;

    static {
        try {
            standWordList = FileUtils.readLines(new File("C:\\Users\\RD\\Desktop\\标准科目及分词.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void markParse(File html, JSONObject jsonObject) throws IOException {
        long before = System.currentTimeMillis();
        System.out.println(before);
        File f_one = html;
//        Set<String> pathList = new LinkedHashSet<>();


        String fileName = FilenameUtils.getBaseName(FilenameUtils.getBaseName(f_one.getName()));
        Document doc = null;
        try {
            doc = Jsoup.parse(new File(f_one.getAbsolutePath()), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Elements children = doc.body().getElementById("div_content").children();



        children.forEach(element -> {
            //不是一个表元素  或者 不是一个含有标准目录的表 不作处理
            if (!isTableElement(element) || !isStandCatalogTable(element)) {
                return;

            }

            String text = getTableText(element);//表格临近文本

            String tableCatalogPath = getTableCatalogPath(element);//目录路径

            StringBuilder resultPathStr = new StringBuilder();
//            resultPathStr.append(fileName); //发行人

            if (StringUtils.isBlank(tableCatalogPath)) {
                return;
            }
            resultPathStr.append("").append(tableCatalogPath);
            String resultText = text.equals("") ? "" : text;
            resultPathStr.append("").append(resultText);
            //符合，进行表格的标注

                for (Map.Entry<String,Object> en :jsonObject.entrySet()){
                    if (StringUtils.isBlank(en.getKey())) {
                        continue;
                    }
                    System.out.println(en.getKey() + ">>>>>>>>" + resultPathStr.toString());
                    if (en.getKey().equals(resultPathStr.toString())) {
                        Elements tbody = element.getElementsByTag("tbody");
                        tbody.attr("style", "background:red");
                        element.before(new Element("p"));
                        element.previousElementSibling().text(en.getValue().toString());
                        break;
                    }

                }

        });

        //写入数据
        System.out.println("写入数据");
        RandomAccessFile rw = new RandomAccessFile(html, "rw");
        rw.setLength(0);
        rw.write(doc.html().getBytes());
        rw.close();
        System.out.println(System.currentTimeMillis() - before);
    }



    /**
     * @param element
     * @return
     * @Description 获取表格的路径
     * @author wangshipeng
     * @date 2019年08月15日 16:23:28
     */
    private String getTableCatalogPath(Element element) {
        String refCatalogId = element.attr("closecatalogid");//表格对应的临近目录
        Element currentElem = element.previousElementSibling();
        while (currentElem != null && !currentElem.id().equals(refCatalogId)) {
            currentElem = currentElem.previousElementSibling();
        }
        if (currentElem == null) {
            return StringUtils.EMPTY;
        }
        LinkedList<String> pathList = new LinkedList<>();
        pathList.addFirst(currentElem.attr("clearcatalogtitle"));

        addCatalogToList(pathList, currentElem);

        String join = "";

        if (CollectionUtils.isEmpty(pathList)) {
            return StringUtils.EMPTY;
        }


        if (pathList.size() == 1) {
            join = pathList.get(0) + "";
            return join;
        }

        StringBuilder manyJoin = new StringBuilder();
        for (int i = 0; i < pathList.size(); i++) {
            if (i == 0) {
                manyJoin.append(pathList.get(0)).append("");
            } else {
                manyJoin.append(pathList.get(i)).append("");
            }
        }
        return manyJoin.substring(0, manyJoin.length());

    }

    private void addCatalogToList(LinkedList<String> pathList, Element element) {
        String parentIds = element.attr("parentids");
        if (StringUtils.isBlank(parentIds)) {
            return;
        }
        String[] parentIdArr = parentIds.split(",");

        String parentId = parentIdArr[0];
        while (element != null && !element.id().equals(parentId)) {
            element = element.previousElementSibling();
        }
        if (element == null) {
            return;
        }
        pathList.addFirst(element.attr("clearcatalogtitle"));


        addCatalogToList(pathList, element);
    }

    /**
     * @param element
     * @return
     * @Description 获取表格临近文本
     * @author wangshipeng
     * @date 2019年08月15日 16:21:32
     */
    private String getTableText(Element element) {
        Element prevElem = element.previousElementSibling();
        if (prevElem == null) {
            return StringUtils.EMPTY;
        }
        //如果是目录或者一个表格元素
        if (prevElem.attr("iscatalog").equals("true") || isTableElement(prevElem)) {
            return StringUtils.EMPTY;
        }

        String tableText = "";

        if (prevElem.text().contains("单位")) {
            tableText = getTableText(prevElem);
        } else {
            return prevElem.text();
        }

        return tableText;
    }


    /**
     * @param element
     * @return
     * @Description 是否是一个表格元素 <table></table>
     * @author wangshipeng
     * @date 2019年08月14日 10:45:19
     */
    public boolean isTableElement(Element element) {
        return element.tagName().equals("table");
    }

    /**
     * @param element
     * @return
     * @Description 表格是否要被处理（只要有一个单元格符合标准目录名称就可以被处理）
     * @author wangshipeng
     * @date 2019年08月16日 09:32:32
     */
    public boolean isStandCatalogTable(Element element) {
        Elements tds = element.select("td");
        for (Element td : tds) {
            if (standWordList.contains(td.text())) {
                return true;
            }
        }

        return false;


    }

    public void outputToText(Set<String> sentenceList, File file) {
        StringBuilder stringBuilder = new StringBuilder();
        sentenceList.forEach(sentence -> stringBuilder.append(sentence).append("\n"));
        try {
            FileUtils.writeStringToFile(file, stringBuilder.toString(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
