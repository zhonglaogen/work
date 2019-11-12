package com.rd.hcb.demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 路由器负责端口映射（路由表）来寻找主机
 * 打洞就是要在不知道对方ip的两台内网机器进行点对点的通信，通过连接外网，产生端口，再将这个端口发送给对方，互相通信一次
 * 就可完成点对点的通信。主要是未知对方的ip和端口（此服务开发的端口），发送到server的发送端口会被利用进行点对点的通信
 * @author zlx
 * @date 2019-11-05 19:51
 */
public class UdpServer {
    public static void main(String[] args) throws IOException {
        DatagramSocket ser = new DatagramSocket(8888);
        ser.setSendBufferSize(20);

        String str = "一二";
        DatagramPacket d;
        byte[] b = new byte[1000];
        d = new DatagramPacket(b, 10);

        while (true) {
            //阻塞方法
            //接受数据
            ser.receive(d);

            byte[] data = d.getData();
//        InputStream inputStream = new ByteArrayInputStream(data);
            System.out.println(ser.getPort());
            System.out.println(ser.getInetAddress());
            System.out.println(d.getAddress());
            System.out.println(d.getPort());
            System.out.println(d.getSocketAddress());
//        int read = inputStream.read(data);
            System.out.println(new String(data, "UTF-8").trim());
//            System.out.println(new String(data, 0, d.getLength(), "UTF-8"));
        }

    }
}
