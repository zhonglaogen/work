package com.rd.hcb.util.message;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬取公司
 *
 * @author zlx
 * @date 2019-12-18 20:03
 */
//{"variable":"对欺诈发行上市的股份购回承诺&公司控股股东、实际控制人钟&详见本招股说明书“第十节投资者保护”之“四、发行人、发行人股东、实际控制人、发行人董事、监事、高级管理人员、&序号","stad_subject":"","stad_primitive":""}
public class CompanyMessage {
    public static final String PRECOMPANYURL = "http://shop.99114.com/list/pinyin/I_";

    /**
     * 公司名称，联系方式(电话,邮箱，传真，地址)，content网站类型，网址
     *
     * @throws IOException
     */


    @Test
    public void test() throws IOException {
        //此处是根据首字母获取的公司名称
        List<StringBuilder> sbl = new ArrayList<>();
        int i = 1;
        do {
            int status = getName(PRECOMPANYURL + i, sbl);
            if (status == 1) {
                break;
            }
            i++;
        } while (true);

    }

    private int getName(String url, List<StringBuilder> sbs) throws IOException {
        Connection connect = Jsoup.connect(url);
        Document document = connect.timeout(10000).get();

        Element allCompanyUl = document.getElementsByClass("cony_div").first();
        Elements lis = allCompanyUl.getElementsByTag("li");
        int index = 1;
        if (lis.size() <= 1) {
            return 1;
        }
        for (Element li : lis) {
            Elements a = li.getElementsByTag("a");
            for (Element e : a) {
//                System.out.println(e.text());
                //获取此链接下的详细内容
                String contentUrl = e.attr("href");
                StringBuilder sb = new StringBuilder();
                sb.append(e.text() + ",");
                getContentMessage(contentUrl, sb);
                sbs.add(sb);
            }
        }
        return 0;
    }


    private void getContentMessage(String contentUrl, StringBuilder sb) throws IOException {
        Connection contentCon = Jsoup.connect(contentUrl);
        Document contentDoc = contentCon.timeout(10000).get();
        Elements addIntroL = contentDoc.getElementsByClass("addIntroL");
        if (addIntroL.size() > 0) {
//            System.out.println("type1-------------------size :" + addIntroL.size());
            Element first = addIntroL.first();
            Elements addR = first.getElementsByClass("addR");
            for (Element e : addR) {
                sb.append(e.text() + ",");
            }

            sb.append("0" + ",");
//            sb.append(contentUrl + ",");

        } else {
            addIntroL = contentDoc.getElementsByClass("y_b_qiye_text positionR clearfix");
            if (addIntroL.size() > 0) {
//                System.out.println("type2-------------------size :" + addIntroL.size());
                Element first = addIntroL.first();
                //获取电话
                Element y_b_qiye_fd = first.getElementsByClass("y_b_qiye_fd").first();
                Element element = y_b_qiye_fd.getElementsByTag("p").get(1);
                String phone = element.text();
                phone = getStringText(phone);
                sb.append(phone + ",");

                //获取邮箱传真地址（0,1,3）
                Elements p = first.getElementsByTag("p");
                Element email = p.get(0);
                String emailText = email.text();
                emailText = getStringText(emailText);
                sb.append(emailText + ",");

                Element chuanZhen = p.get(1);
                String chuanZhenText = chuanZhen.text();
                chuanZhenText = getStringText(chuanZhenText);
                sb.append(chuanZhenText + ",");

                Element addr = p.get(3);
                String addText = addr.text();
                addText = getStringText(addText);
                sb.append(addText + ",");

                sb.append("1" + ",");

            } else {
                sb.append(",,,,");
                sb.append("3" + ",");
//                System.out.println("type3********************************" + contentUrl);
            }
            sb.append(contentUrl);
        }
        System.out.println(sb.toString());
    }

    private String getStringText(String text) {
        int index = text.indexOf("：");
        return text.substring(index + 1).trim();
    }
}
