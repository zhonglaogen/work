package com.rd.hcb.test;

import com.rd.hcb.task.entity.ConfigFile;
import com.rd.hcb.task.entity.PeoPle;
import org.junit.Test;


import java.io.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class ChuanZhiTiao implements Serializable {

    static Integer sta = 1;

    static {
        sta = 11;
    }


    public static void main(String[] args) throws IOException {


//        File curfile=new File("d:\\txt1.txt");
//        RandomAccessFile rw = new RandomAccessFile(curfile, "rw");
//        FileChannel channel = rw.getChannel();
//        FileOutputStream fo = new FileOutputStream("d:\\txt.txt");
//        fo.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
//        fo.close();

        System.out.println("s".getBytes().length);
        char a = '西';
        System.out.println(a);
        System.out.println(Character.SIZE);

        byte[] aa = new byte[3];
        for (int i = 0; i < aa.length; i++) {
            System.out.println(aa[i]);
        }
        byte a1 = -1;
        System.out.println(a1);

        System.out.println("----------------------");

        StringBuilder sb = new StringBuilder();
        System.out.println(sb.toString().equals(""));

        String teststr = "(b（示范法各嘎多个。集合";
        boolean b = teststr.matches("^[\\(,（].*");
        System.out.println(b);

        System.out.println("----------------------");


        System.out.println("----------------------");

        new Thread(() -> {
            while (true) {
            }
        });
        System.out.println("create thread ……");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);


    }

    /**
     * 传纸条问题
     */
    private static void note() {
        int[][] a = {{0, 0, 0, 0},
                {0, 0, 3, 9},
                {0, 2, 8, 5},
                {0, 5, 7, 0}};
        int n = 3;
        int m = 3;
//        四个for循环形容的是两条路，两个max比较的是 去的 1 ，2 和回来的 1， 2进行的两两组合
//        寻找最大值，i代表去的横坐标，j代表去的纵坐标，k代表回来的横坐标，l代表回来的纵坐标

        int[][][][] f = new int[9][9][9][9];
        int i, j, k, l;
        for (i = 1; i <= n; i++)
            for (j = 1; j <= m; j++)
                for (k = 1; k <= m; k++)
                    for (l = 1; l <= n; l++) {
                        int x = Math.max(f[i - 1][j][k - 1][l], f[i][j - 1][k][l - 1]);
                        int y = Math.max(f[i - 1][j][k][l - 1], f[i][j - 1][k - 1][l]);

//                        不做操作，去的路和回来的路一样，不加回来的路，所以值会小不会选
//                        这里去的路和回来的路任何一个点都不能相同
                        if (i != k && j != l)
                            f[i][j][k][l] += a[k][l];
                    }
        System.out.println(f[n][m][n][m]);
    }

    private static void testArry() {
        int[] one = {1, 2, 3, 4};
        int[][] two = {{1, 2, 3, 4}, {1, 2, 3, 4}};
        int[][][] three = {{{1, 2, 3}, {1, 2, 3}}, {{1, 2, 3}, {1, 2, 3}}, {{1, 2, 3}, {1, 2, 3}}};
        int[][][][] four = {
                {
                        {
                                {
                                        1, 2, 3
                                }
                        }
                }
        };
    }



    @Test
    public void test(){
        BigDecimal t1= new BigDecimal("0.3");
        BigDecimal t2=new BigDecimal("0.3623");
        t1.setScale(4,BigDecimal.ROUND_HALF_UP);
        t2.setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal subtract = t2.subtract(t1);

        System.out.println(t2.compareTo(t1));
      String a = "123321的12";
      String b = "1的2";
        System.out.println(a.contains(b));
        System.out.println(LocalDate.now().getYear());

        System.out.println("*******************");
        HashMap<String, String> h1 = new HashMap<>();
        System.out.println(h1.get("aaa"));

        List<Integer> l1 = new ArrayList<>();
        l1.add(1);
        l1.add(2);
        l1.add(3);
        List<Integer> l2 = new ArrayList<>();
        l2.add(3);
        l2.add(4);
        l2.add(5);
        l1.addAll(l2);
        System.out.println(l1);
        System.out.println("项目".equals("项目"));
        String xx= "2014年4.22.";
        System.out.println(xx.substring(0,4));

        PeoPle p1 = new PeoPle();
        p1.setId("1");
        PeoPle p2 = new PeoPle();
        p2.setId("1");
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());
        Integer i1 = null;
        try {
            i1 = Integer.valueOf("13o3");
            int i = 1/0;
            System.out.println("0000000");
        }catch (Exception e){
            System.out.println("捕获");
        }
        System.out.println("11111");
        System.out.println(i1);


    }

    @Test
    public void test1() throws IOException {
        Properties prop = new Properties();
        prop.load(this.getClass().getResourceAsStream("/test.properties"));
        String userName = prop.getProperty("NAME");
        System.out.println(userName);
        String subIndex = "1010-2020-3030";
        int i = subIndex.indexOf("-");
        System.out.println(i);
        System.out.println(subIndex.substring(0, i));
        List<PeoPle> ps = new ArrayList<>();
        ps.add(new PeoPle("1","a"));
        ps.add(new PeoPle("1","b"));
        ps.add(new PeoPle("1","c"));
        Map<String, List<PeoPle>> collect = ps.stream().filter(x -> x.getId().equals("2")).collect(Collectors.groupingBy(y ->{
            String id = y.getId();
             id = id + "2";
             id.substring(0,1);
             return id;
                }

        ));
        System.out.println(collect.size());
        System.out.println("****************");
        Thread t1 = new Thread(()->{
            System.out.println(11);
            System.out.println(Thread.currentThread().getState());
        });
        t1.start();

    }

    @Test
    public void test11(){
        //日志记录
        //后台计算规则，静态爬取公司信息入库，
        //申请注册信息，公司信息库查不到，动态去爬取信息，如果爬取到此公司信息动态获取邮箱，会邮件通知此公司
        //分词，用来对求职信息进行解析
        //https://www.kanzhun.com/companyl/search/?q=&pagenum=3&ka=paging3
        //excel的解析和导出，下载模板excel，进行信息的解析
        //html的解析.excel转换html在线查看
        //企业信息智能在线计算，计算企业入职离职信息
        System.out.println(sta);
    }

}
