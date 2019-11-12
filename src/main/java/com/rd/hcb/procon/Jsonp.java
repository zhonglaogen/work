package com.rd.hcb.procon;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

import static com.rd.hcb.procon.FileConsumer.consumer;
import static com.rd.hcb.procon.FileProducer.producer;

/**
 * @author zlx
 * @date 2019-11-01 09:23
 */
public class Jsonp {

    public static String urlstr1 = "http://query.sse.com.cn/commonSoaQuery.do?jsonCallBack=jsonpCallback14132&extTeacher=&extWTFL=&stockcode=&siteId=28&sqlId=BS_KCB_GGLL&extGGLX=&channelId=10007,10008,10009,10010&createTime=&createTimeEnd=&extGGDL=&order=createTime|desc,stockcode|asc&isPagination=true&pageHelp.pageSize=15&pageHelp.pageNo=zlxpage&pageHelp.beginPage=1&pageHelp.cacheSize=1&pageHelp.endPage=5&type=&_=1572573565776";
    public static String urlstr2 = "http://query.sse.com.cn/commonSoaQuery.do?jsonCallBack=jsonpCallback11019&siteId=28&sqlId=BS_KCB_GGLL&extGGLX=&stockcode=&channelId=10743,10744,10012&extGGDL=&order=createTime|desc,stockcode|asc&isPagination=true&pageHelp.pageSize=15&pageHelp.pageNo=zlxpage&pageHelp.beginPage=1&pageHelp.cacheSize=1&pageHelp.endPage=5&type=&_=1572854107331";
    /**
     * 负责将此url的文件下载，
     */
    static BlockingQueue<Map<String, String>> downloadQueue = new ArrayBlockingQueue(5000);


    public static void main(String[] args) throws IOException {
        //监管措施
        try {
            jianGuanCuoShi(urlstr1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }



        // 监管问询
//        try {
//            jianGuanWenXun(urlstr2);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }


    }


    private static void jianGuanCuoShi(String urlstr) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //主线程
        producer(urlstr, "getMap1","D:\\test.txt");


        //另外启动线程
//        consumer("D:\\downFilePath\\监管措施\\pdf1");
        //解析html数据
//        getHtmlData1();
    }

    private static void jianGuanWenXun(String urlstr) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        //主线程
        producer(urlstr, "getMap2","D:\\监管问询.txt");

        //另外启动线程
        consumer("D:\\downFilePath\\监管问询\\pdf1");

        //解析html数据
//        getHtmlData2();
    }




}
