
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;


import java.io.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class Hello implements Serializable {


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

    private static void note() {
        int[][] a = {{0, 0, 0, 0}, {0, 0, 3, 9},
                {0, 2, 8, 5},
                {0, 5, 7, 0}};
        int n = 3;
        int m = 3;

        int[][][][] f = new int[9][9][9][9];
        int i, j, k, l;
        for (i = 1; i <= n; i++)
            for (j = 1; j <= m; j++)
                for (k = 1; k <= m; k++)
                    for (l = 1; l <= n; l++) {
                        int x = Math.max(f[i - 1][j][k - 1][l], f[i][j - 1][k][l - 1]);
                        int y = Math.max(f[i - 1][j][k][l - 1], f[i][j - 1][k - 1][l]);
                        f[i][j][k][l] = Math.max(x, y) + a[i][j];
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



}
