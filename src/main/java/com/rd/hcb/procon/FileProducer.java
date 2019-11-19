package com.rd.hcb.procon;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.rd.hcb.procon.Jsonp.downloadQueue;

/**
 * @author zlx
 * @date 2019-11-01 17:30
 */
public class FileProducer {


    /**
     * 生出pdf的url,消费者消费完成后，pdf转换为HTML，再次调用将网页的数据和pdf一并写入文件中
     *
     * @param urlstr
     * @param methodName
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static void producer(String urlstr, String methodName, String dataFile) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        //输出的位置
        File objectFile = new File(dataFile);
        RandomAccessFile rw = new RandomAccessFile(objectFile, "rw");
        rw.write("股票代码@公司简称@更新日期@监管类型@文件名称@涉及对象@附件@文件号@涉及对象简介@违反是由@违反法律法规@\n".getBytes());
        //获取集合JSONArray
        JSONArray jsonArray = null;
        //启动

        try {
            //可设置爬取的页数，数据如果不是按照时间进行排序，此参数可指定要扫描的页数，sort表示数据是否按照时间进行排序
            jsonArray = httpConnection(urlstr, 3, -30, true);
        } catch (ParseException e) {
            System.out.println("#日期转换错误");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        Class<FileProducer> fileProducerClass = FileProducer.class;
        Method method = fileProducerClass.getDeclaredMethod(methodName, JSONArray.class, RandomAccessFile.class, int.class);
        method.setAccessible(true);
        method.invoke(null, jsonArray, rw, 1);
        rw.close();
    }


    /**
     * 获取监管措施信息
     *
     * @param jsonArray
     * @param rw        要写入的文件
     * @param opt       0为下载文件操作，1为解析文件操作
     */
    private static void getMap1(JSONArray jsonArray, RandomAccessFile rw, int opt) throws IOException {
        int size = jsonArray.size();
        //给控制台标识序列号
        int i = 1;
        //给文件标识序列号
        int j = -2;


        //将得到的数组进行map封装
        for (Object jsonObject : jsonArray) {
            JSONObject jo = (JSONObject) jsonObject;
            Map<String, String> map = new HashMap<>();
            System.out.println("股票代码为stockcode：" + jo.getString("stockcode"));
            System.out.println("公司简称extGSJC：" + jo.getString("extGSJC"));
            System.out.println("更新日期createTime：" + jo.getString("createTime"));
            System.out.println("监管类型extTYPE：" + jo.getString("extTYPE"));
            System.out.println("文件名称docTitle：" + jo.getString("docTitle"));
            System.out.println("涉及对象extTeacher：" + jo.getString("extTeacher"));
            System.out.println("文档地址docURL：" + jo.getString("docURL"));
            System.out.println("-----------------------数据行数" + i++ + "-------------------------------------");
            map.put("stockcode", jo.getString("stockcode"));
            map.put("extGSJC", jo.getString("extGSJC"));
            map.put("createTime", jo.getString("createTime"));
            map.put("extTYPE", jo.getString("extTYPE"));
            map.put("docTitle", jo.getString("docTitle"));
            map.put("extTeacher", jo.getString("extTeacher"));
            map.put("docURL", "http://" + jo.getString("docURL"));


            //已经下载文件，进行解析
            if (opt != 0) {
                rw.write((jo.getString("stockcode") + "@").getBytes());
                rw.write((jo.getString("extGSJC") + "@").getBytes());
                rw.write((jo.getString("createTime") + "@").getBytes());
                rw.write((jo.getString("extTYPE") + "@").getBytes());
                rw.write((jo.getString("docTitle") + "@").getBytes());
                rw.write((jo.getString("extTeacher") + "@").getBytes());
                rw.write((jo.getString("docURL") + "@").getBytes());
                String createTime = jo.getString("createTime").replaceAll(":| ", "");

                getHtmlData1(new File("D:\\downFilePath\\监管措施\\clearhtml\\" +
                        j + "#" + jo.get("stockcode") + createTime + ".docx.html"), rw);
//                getHtmlData1(new File("D:\\downFilePath\\监管措施\\clearhtml\\" + map.get("docTitle") + ".docx.html"), rw);
                j++;

                //此条数据结束
                rw.write("\n".getBytes());
            } else {
                //为下载文件，进行下载
                try {
                    //将下载任务添加至阻塞队列，设置超时时间为10秒
                    downloadQueue.offer(map, 10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 获取监管问询信息
     *
     * @param jsonArray
     * @param rw        要写入的文件
     * @param opt       0为下载文件操作，1为解析文件操作
     */
    private static void getMap2(JSONArray jsonArray, RandomAccessFile rw, int opt) throws IOException {

        int size = jsonArray.size();
        //给控制台标识序列号
        int i = 1;
        //给文件标识序列号
        int j = 0;
        //将得到的数组进行map封装
        for (Object jsonObject : jsonArray) {
            JSONObject jo = (JSONObject) jsonObject;
            Map<String, String> map = new HashMap<>();
            System.out.println("股票代码为stockcode：" + jo.getString("stockcode"));
            System.out.println("公司简称extGSJC：" + jo.getString("extGSJC"));
            System.out.println("更新日期createTime：" + jo.getString("createTime"));
//            System.out.println("监管类型extTYPE：" + jo.getString("extTYPE"));
            System.out.println("监管类型extWTFL：" + jo.getString("extWTFL"));
            System.out.println("文件名称docTitle：" + jo.getString("docTitle"));
//            System.out.println("涉及对象extTeacher：" + jo.getString("extTeacher"));
            System.out.println("文档地址docURL：" + jo.getString("docURL"));
            System.out.println("-----------------------数据行数" + i++ + "-------------------------------------");

            map.put("stockcode", jo.getString("stockcode"));
            map.put("extGSJC", jo.getString("extGSJC"));
            map.put("createTime", jo.getString("createTime"));
            map.put("extWTFL", jo.getString("extWTFL"));


            map.put("docTitle", jo.getString("docTitle"));


//            map.put("extTeacher",jo.getString("extTeacher"));
            map.put("docURL", "http://" + jo.getString("docURL"));

            //数据解析
            if (opt != 0) {
                rw.write((jo.getString("stockcode") + "@").getBytes());
                rw.write((jo.getString("extGSJC") + "@").getBytes());
                rw.write((jo.getString("createTime") + "@").getBytes());
                rw.write((jo.getString("extWTFL") + "@").getBytes());
                rw.write((jo.getString("docTitle") + "@").getBytes());
                rw.write((jo.getString("docURL") + "@").getBytes());


                String createTime = jo.getString("createTime").replaceAll(":| ", "");


                getHtmlData2(new File("D:\\downFilePath\\监管问询\\clearhtml\\" + j + "#" + map.get("stockcode") + createTime + ".docx.html"), rw);
                j++;
                rw.write("\n".getBytes());
            } else {
                //文件下载
                try {
                    //将下载任务添加至阻塞队列，设置超时时间为10秒
                    downloadQueue.offer(map, 10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }


    /**
     * 获取网页的表格内的每条数据
     *
     * @param datasCount 获取与当前日期相差天数的数据，负数代表小于当前日期的天数
     * @param page       获取的页数 ,可不输入，可输入
     * @param sortTime   表格数据是否是按照时间进行展示
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private static JSONArray httpConnection(String urlstr, int page, int datasCount, boolean sortTime) throws Exception {
        if (page < 0) {
            throw new Exception("输入页数不能为负数");
        }

        //所有的符合条件的表格数据
        JSONArray allDatas = null;
        if (page > 0) {
            allDatas = new JSONArray();
        }
        for (int i = 1; i <= page; i++) {
            // 保存接收到的cookie
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));


            String netUrl = urlstr.replaceFirst("zlxpage", String.valueOf(i));
            System.out.println(netUrl);
            URL url = new URL(netUrl);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setUseCaches(true);
            httpurlconnection.setRequestProperty("Accept", "*/*");
//        httpurlconnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpurlconnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            httpurlconnection.setRequestProperty("Pragma", "no-cache");
            httpurlconnection.setRequestProperty("Cache-Control", "no-cache");
            httpurlconnection.setRequestProperty("Connection", "keep-alive");
//        httpurlconnection.setRequestProperty("Host", "www.neeq.com.cn");
//        httpurlconnection.setRequestProperty("Accept-Encoding", "deflate");//gzip会乱码
            httpurlconnection.setRequestProperty("Referer", "http://www.sse.com.cn/disclosure/credibility/supervision/measures/");
            httpurlconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
//        httpurlconnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
//        httpurlconnection.setRequestProperty("Transfer-Encoding", "chunked");
            httpurlconnection.setRequestMethod("GET");

            httpurlconnection.connect();
            InputStream inputStream = httpurlconnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] read = new byte[1024 * 10 * 10];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = bufferedInputStream.read(read)) != -1) {
                sb.append(new String(read, 0, len));
            }
            inputStream.close();
            String s = sb.toString();
            System.out.println(s);
            int startSub = s.indexOf("(");
            int endSub = s.lastIndexOf(")");
            String jsonStr = s.substring(startSub + 1, endSub);
            JSONObject parse = (JSONObject) JSONObject.parse(jsonStr);
            System.out.println(jsonStr);

            //获取pageHelp
            JSONObject pageHelp = parse.getJSONObject("pageHelp");
            //获取当前页的所有json集合
            JSONArray datas = pageHelp.getJSONArray("data");

            //近七天的数据
            AllUtils.findSevenData(datas, datasCount);


            allDatas.addAll(datas);
            //此处为每页数据的条目,小于每页的条数，并且按照时间顺序进行展示
            if (datas.size() < 15 && sortTime) {
                break;
            }
        }

        return allDatas;


    }

    /**
     * 监管措施文件内容获取
     *
     * @param file 要进行数据提取的文件
     * @param rw   数据输出路径
     * @throws IOException
     */
    public static void getHtmlData1(File file, RandomAccessFile rw) throws IOException {
        if (!file.exists()) {
            return;
        }
        Document parse = Jsoup.parse(file, "utf-8");
        Element body = parse.body();

        Element p = body.getElementsByTag("p").first();
        //文件号的获取
        while (!p.text().contains("号")) {
            p = p.nextElementSibling();
            if (p == null) {
                System.out.println("没有文件号");
                return;
            }
        }
        String fileNumberText = p.text();
        int numEndIndex = fileNumberText.indexOf("号");
        fileNumberText = fileNumberText.substring(0, numEndIndex + 1);

        //输出文件号
        System.out.println("文件号为：" + fileNumberText);
        rw.write((fileNumberText + "@").getBytes());

        System.out.println("---------------------------------");
        p = body.getElementsByTag("p").first();


        while (!p.text().contains("当事人：")) {
            p = p.nextElementSibling();
            if (p == null) {
                System.out.println("没有当事人");
                rw.write(("@").getBytes());
                return;
            }
        }
        do {
            p = p.nextElementSibling();
            if (p == null) {
                System.out.println("当事人没有正常的句号结束");
                return;
            }
            //获取当事人
            System.out.println(p.text());
            rw.write((p.text() + "\t").getBytes());
        } while (!p.text().contains("。"));
        //当事人列结束
        rw.write(("@").getBytes());


        System.out.println("---------------------------------");
        p = body.getElementsByTag("p").first();
        //违法是由的获取
        //替换数字
        String match = "(\\d|[一二三四五六七八九十])+";
        //经查明信息
        StringBuilder sb = new StringBuilder();
        while ((p = p.nextElementSibling()) != null) {
            if (p.attr("class").equals("mark") && p.text().contains("、")) {
                //保存一级目录
                int oneTextIndex = p.text().indexOf("、");
                String keyWord = p.text().substring(0, oneTextIndex + 1).replaceAll(match, "*");
                //输出一级目录
                sb.append(p.text() + "  ");

                //输出经查询正文
                p = p.nextElementSibling();
                if (p.text().contains("经查明")) {
                    //弥补换行不精确，结尾不是。就遍历下一个
                    do {
                        //包含结尾，结束
                        if (p.text().endsWith("。")) {
                            break;
                        } else {
                            sb.append(p.text());
                        }
                        p = p.nextElementSibling();
                        if (p == null) {
                            //未找到结尾是。的元素
                            break;
                        }
                    } while (!p.text().endsWith("。"));
                    //将包含。的最后一行存入
                    sb.append(p.text() + "  ");
                } else if (p.attr("class").equals("mark") && p.text().matches("^[\\(,（].*")) {
                    //为二级级目录
                    //输出二级目录
                    sb.append(p.text() + "  ");
                }


                //匹配子目录
                while ((p = p.nextElementSibling()) != null) {
                    //匹配到下一个一级目录
                    if (p.attr("class").equals("mark") && p.text().replaceAll(match, "*").contains(keyWord)) {
                        break;
                    }
                    //输出二级目录标题
                    //解决目录标题解析异常，硬编码中文（ 或英文(
                    if (p.attr("class").equals("mark") && p.text().matches("^[\\(,（].*")) {
                        //输出二级目录
                        sb.append(p.text() + "  ");
                    }
                }
                //寻找结束
                sb.append("@");
                //已经找到经查询内容
                break;
            }
            //只包含经查询内容
            if (p.text().contains("经查明")) {
                //弥补换行不精确，结尾不是。就遍历下一个
                do {
                    //包含结尾，结束
                    if (p.text().endsWith("。")) {
                        break;
                    } else {
                        sb.append(p.text());
                    }
                    p = p.nextElementSibling();
                    if (p == null) {
                        //未找到结尾是。的元素
                        break;
                    }
                } while (!p.text().endsWith("。"));
                //将包含。的最后一行存入
                sb.append(p.text());
                sb.append("@");
                break;
            }
            if (p == null) {
                sb.append("不包含违反是由@");
                break;
            }
        }
        //将违反是由输出
        if (!sb.toString().equals("")) {
            System.out.println(sb.toString());
            rw.write(sb.toString().getBytes());
        } else {
            //不包含违反是由
            System.out.println("不包含违反是由@");
            rw.write("不包含违反是由@".getBytes());
        }


        System.out.println("---------------------------------");
        //违反法律法规的固定句式获取，获取规则为书名号&阿拉伯数字的一句话
        String[] split = body.text().split("，|。");
        int length = split.length;
        String regex = ".*(《.*》)+.*(\\d)+.*条.*";
        for (int i = 0; i < length; i++) {
            if (split[i].matches(regex)) {
                System.out.println(split[i]);
                rw.write((split[i] + ";").getBytes());
            }
        }
        rw.write(("@").getBytes());
    }

    @Test
    public void test() throws FileNotFoundException {
        try {
            getHtmlData1(new File("D:\\downFilePath\\监管措施\\clearhtml\\17#6000692019-10-14191243.docx.html")
                    , new RandomAccessFile("D:\\singole.txt", "rw"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 监管问询文件内容获取
     *
     * @throws IOException
     */
    public static void getHtmlData2(File file, RandomAccessFile rw) throws IOException {
        if (!file.exists()) {
            return;
        }

        Document parse = Jsoup.parse(file, "utf-8");
        Element body = parse.body();

        Element p = body.getElementsByTag("p").first();
        //文件号的获取
        while (!p.text().contains("号")) {
            p = p.nextElementSibling();
            if (p == null) {
                System.out.println("没有文件号");
                return;
            }
        }
        String fileNumberText = p.text();
        int numEndIndex = fileNumberText.indexOf("号");
        fileNumberText = fileNumberText.substring(0, numEndIndex + 1);

        //输出文件号
        System.out.println("文件号为：" + fileNumberText);
        rw.write((fileNumberText + "@").getBytes());
    }


}
