package com.rd.hcb.procon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author zlx
 * @date 2019-11-01 17:31
 */
public class FileConsumer {

    public static void consumer(String downLoadPath) {
        Future<?> submit = SingolePool.getInstance().submit(() -> {
            try {

                int i=1;
                while (true) {
                    Map<String, String> take = Jsonp.downloadQueue.poll(15, TimeUnit.SECONDS);
                    if (take == null || take.size() == 0) {
                        break;
                    }
                    String createTime = take.get("createTime").trim().replaceAll(":| ", "");
//                    downloadFile(take.get("stockcode")+createTime, take.get("docURL"), downLoadPath,i);
                    downloadFile(take.get("docTitle"),take.get("docURL"),downLoadPath,i);
                    System.out.println(i++);
                }

            } catch (InterruptedException e) {
                System.err.println("消费线程消费异常中断");
                e.printStackTrace();
            } catch (MalformedURLException e) {
                System.err.println("网络url异常");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("io流异常");
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("文件夹不存在");
                e.printStackTrace();
            }
            System.out.println("本次下载任务完成");
            //执行转换操作
            //执行解析任务
            //再次执行爬虫方法，将解析和爬虫的数据一起存放到文件中
            return "消费任务结束";
        });

    }

    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @param docURL   文件的url
     * @param downPath 文件保存的路径
     */
    private static void downloadFile(String fileName, String docURL, String downPath,int i) throws Exception {

        //Windows下，检验是否是以'\'结尾
        if (!downPath.endsWith("\\")) {
            downPath = downPath + "\\";
        }
        //文件夹不存在
        File dir = new File(downPath);
        if (!dir.exists() || dir.isFile()) {
            throw new Exception("指定文件夹不存在");
        }

        //通过地址下载文件，fileName为stockcode（股票代码）
        URL fileUrl = new URL(docURL);
        HttpURLConnection httpUrl = null;

        httpUrl = (HttpURLConnection) fileUrl.openConnection();
        System.out.println("#文件地址流锁定,将要下载文件至" + downPath+"\\"+fileName+".pdf");

        httpUrl.setConnectTimeout(50000);

        httpUrl.setReadTimeout(100000);
        httpUrl.setRequestProperty("accept", "*/*");
        httpUrl.setRequestProperty("connection", "Keep-Alive");
        httpUrl.setRequestProperty("user-agent", AllUtils.getRandomUserAgent());
        httpUrl.connect();
        InputStream fi = httpUrl.getInputStream();
        //保存的路径
//        FileOutputStream fo = new FileOutputStream(downPath +i+"#"+fileName + ".pdf");
        FileOutputStream fo = new FileOutputStream(downPath+fileName + ".pdf");
        int len = 0;
        //1mb,2^10*2^10<2^31
        byte[] bytes = new byte[1024 * 1024];
        while ((len = fi.read(bytes)) != -1) {
            fo.write(bytes, 0, len);
        }
        fi.close();
        fo.flush();
        fo.close();

    }
}
