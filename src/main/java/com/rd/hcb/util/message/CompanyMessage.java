package com.rd.hcb.util.message;

import com.rd.hcb.procon.AllUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 爬取公司
 *
 * @author zlx
 * @date 2019-12-18 20:03
 */

public class CompanyMessage {
    //s 1165
    public static final String PRECOMPANYURL = "http://shop.99114.com/list/pinyin/S_";

    /**
     * 公司名称，联系方式(电话,邮箱，传真，地址)，content网站类型，网址
     *
     * @throws IOException
     */


    @Test
    public void test() throws IOException, InterruptedException {
        //此处是根据首字母获取的公司名称
        List<StringBuilder> sbl = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        int i = 1;
        int error = 0;
        System.out.println("正在获取数据……");
        do {
            System.out.println("第" + i + "页数据");
            int status = 0;
            try {
                 status = getName(PRECOMPANYURL + i, result);
            }catch (Exception e){
                e.printStackTrace();
//                页面获取异常
                status = 3;
            }catch (Error e){
                e.printStackTrace();
                error++;
//                页面获取异常
                status = 3;
            }

            int sleep = i % 6;
            if (sleep == 0) {
                //每10页存一次
                System.out.println("正在写入数据……");
                System.out.println(error);
                error = 0;
                FileUtil.writeToFileEnd(result.toString(), "D:\\myzlx\\S.txt");
                result = new StringBuilder();
//                TimeUnit.SECONDS.sleep(10);
            }
            if (status == 1) {
                break;
            }
            i++;
        } while (true);
//        System.out.println(result.toString());
        //最后再写一次文件
        if (StringUtils.isNotBlank(result)){
            System.out.println("正在写入数据……");
            FileUtil.writeToFileEnd(result.toString(), "D:\\myzlx\\S.txt");
        }

    }


    private int getName(String url, StringBuilder sbs) throws IOException, InterruptedException {
        Connection connect = Jsoup.connect(url);
        Document document = connect.timeout(50000).userAgent(AllUtils.getRandomUserAgent()).get();

        Element allCompanyUl = document.getElementsByClass("cony_div").first();
        Elements lis = allCompanyUl.getElementsByTag("li");
        int index = 1;
        if (lis.size() <= 1) {
            return 1;
        }
        int sleep = 1;
        for (Element li : lis) {
            Elements a = li.getElementsByTag("a");
            for (Element e : a) {
//                System.out.println(e.text());
                //获取此链接下的详细内容
                String contentUrl = e.attr("href");
//                System.out.println("获取公司信息为：" + e.text());
                StringBuilder sb = new StringBuilder();
                sb.append(e.text() + "~");
                try {
//                    System.out.println(e.text());
                    getContentMessage(contentUrl, sb);
                }catch (Exception err){
                    err.printStackTrace();
                    sb = new StringBuilder(e.text() + "~~~~~3~"+contentUrl);

                }

                sbs.append(sb);
                if (sleep % 16 == 0) {
                    TimeUnit.SECONDS.sleep(3);
                }
                sleep++;
            }
        }
        return 0;
    }


    private void getContentMessage(String contentUrl, StringBuilder sb) throws IOException {
        Connection contentCon = Jsoup.connect(contentUrl);
        Document contentDoc = null;
        try {
             contentDoc = contentCon.timeout(50000).userAgent(AllUtils.getRandomUserAgent()).get();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(contentUrl);
            //响应超时
            sb.append("~~~~");
            sb.append("3" + "~");
            sb.append(contentUrl + "\n");
            return;
        }catch (Error e){
            e.printStackTrace();
            System.out.println(contentUrl);
            //响应超时
            sb.append("~~~~");
            sb.append("3" + "~");
            sb.append(contentUrl + "\n");
            return;
        }

        Elements addIntroL = contentDoc.getElementsByClass("addIntroL");
        //第一种网页类型
        if (addIntroL.size() > 0) {

            Element first = addIntroL.first();
            Elements addR = first.getElementsByClass("addR");
            //有问题
            if(addR.size() == 4){
                for (Element e : addR) {
                    sb.append(e.text() + "~");
                }
            }else {
                //不规范网址
                sb.append("~~~~");
            }


            sb.append("0" + "~");



        } else {
            //第二种网页类型
            addIntroL = contentDoc.getElementsByClass("y_b_qiye_text positionR clearfix");
            if (addIntroL.size() > 0) {

                Element first = addIntroL.first();
                //获取电话
                Element y_b_qiye_fd = first.getElementsByClass("y_b_qiye_fd").first();
                Element element = y_b_qiye_fd.getElementsByTag("p").get(1);
                String phone = element.text();
                phone = getStringText(phone);
                sb.append(phone + "~");

                //获取邮箱传真地址（0,1,3）
                Elements p = first.getElementsByTag("p");
                Element email = p.get(0);
                String emailText = email.text();
                emailText = getStringText(emailText);
                sb.append(emailText + "~");

                Element chuanZhen = p.get(1);
                String chuanZhenText = chuanZhen.text();
                chuanZhenText = getStringText(chuanZhenText);
                sb.append(chuanZhenText + "~");

                Element addr = p.get(3);
                String addText = addr.text();
                addText = getStringText(addText);
                sb.append(addText + "~");

                sb.append("1" + "~");

            } else {
                //找不到网址类型
                sb.append("~~~~");
                sb.append("2" + "~");

            }

        }
        sb.append(contentUrl + "\n");

    }


    private String getStringText(String text) {
        int index = text.indexOf("：");
        return text.substring(index + 1).trim();
    }

    @Test
    public void test222() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        System.out.println("222");
    }
}
