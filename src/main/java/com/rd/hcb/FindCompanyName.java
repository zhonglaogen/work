package com.rd.hcb;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

/**
 * @author zlx
 * @date 2019-12-31 09:51
 */
public class FindCompanyName {
    /**
     * 获取公司简称和释义
     * @param doc
     * @param res
     * @throws IOException
     */
    public void getHtmlCompanyName2(Document doc, Map<String, String> res) throws IOException {


        //是否包含公司
        boolean isContainCompany = false;
        //是否包含释义
        boolean isCatalogExplain = false;
        //包含五个指
        boolean isFiveZhi = false;
        //列数异常
        boolean tdCountError = false;


        Element body = doc.body();
        //获取istitle,并且为一级目录
        Elements istitles = body.getElementsByAttribute("istitle");
//        System.out.println("目录数量：" + istitles.size());
        //遍历目录
        for (Element istitle : istitles) {
            //包含释义
            boolean shiyi = istitle.text().contains("释 义") || istitle.text().contains("释义");
            if (shiyi) {
                //包含释义
                isCatalogExplain = true;
                System.out.println(istitle.text());
                Element table = istitle.nextElementSibling();
                //只遍历前20个表格
                int findTableCount = 0;

                while (true) {
                    if (table.tag().getName().equals("table")) {
                        //包含5个指
                        String st = table.text();
                        int count = (st.length() - st.replace("指", "").length()) / "指".length();
                        System.out.println("指的个数" + count);
                        if (count > 5) {
                            isFiveZhi = true;
                            Element tbody = table.select("tbody").first();

                            Elements trs = tbody.getElementsByTag("tr");
                            for (Element tr : trs) {
                                //遍历包含公司的每一行
                                if (tr.text().contains("公司")) {
                                    //包含公司，则此文件不用复制出来
                                    isContainCompany = true;
                                    //判断列是否为三列，若果是，去掉中间列
                                    Elements tds = tr.getElementsByTag("td");
                                    int tdsSize = tds.size();
                                    if (tdsSize == 3) {
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < tdsSize; i++) {
                                            if (i != 1) {
                                                String tdText = tds.get(i).text() + "@";
                                                sb.append(tdText);
                                            }
                                        }
                                        //对stringbuilder 进行拆分为map
                                        changeToMap(sb, res);
                                        System.out.println();
                                    } else if (tdsSize == 2) {
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < tdsSize; i++) {
                                            String tdText = tds.get(i).text() + "@";
                                            sb.append(tdText);
                                        }
                                        //对stringbuilder 进行拆分为map
                                        changeToMap(sb, res);
                                        System.out.println();
                                    } else {
                                        tdCountError = true;
                                    }


                                }

                            }

                        }
                    }
                    table = table.nextElementSibling();
                    if (table == null || table.attr("class") == null || table.attr("class").contains("onecatalog")) {
//                        到下一层一级目录不包含‘指’
                        break;
                    }
                }
                //已经找到释义标题
                break;
            }

        }
        //判断未读取出的原因
        if (!isCatalogExplain || !isFiveZhi || !isContainCompany || tdCountError) {
            System.out.println("#是否存在释义" + isCatalogExplain);
            System.out.println("#是否含有5个指" + isFiveZhi);
            System.out.println("#是否包含公司" + isContainCompany);
            System.out.println("#是否列数目异常" + tdCountError);
            return;

        }


    }

    /**
     * 将字符串转换为map
     * @param sb
     * @param res
     */
    private void changeToMap(StringBuilder sb, Map<String, String> res) {
        String[] split = sb.toString().split("@");
        res.put(split[0], split[1]);
    }
}
