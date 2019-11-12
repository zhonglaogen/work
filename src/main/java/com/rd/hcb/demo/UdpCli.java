package com.rd.hcb.demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * @author zlx
 * @date 2019-11-05 19:52
 */
public class UdpCli {
    public static void main(String[] args) throws SocketException {
        DatagramSocket cli=new DatagramSocket(6666);
        cli.setSendBufferSize(20);

        String str="是";
        DatagramPacket d;
        d = new DatagramPacket(str.getBytes(StandardCharsets.UTF_8),str.getBytes().length,new InetSocketAddress("127.0.0.1",8888));
        try {
            while (true){
                System.out.println("contion");
                cli.send(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cli.close();
        }


//        System.out.println(cli.getPort());
//        System.out.println(cli.getInetAddress());
//        System.out.println(d.getAddress());
//        System.out.println(d.getPort());
//        System.out.println(d.getSocketAddress());

    }
}
