import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.apache.commons.io.IOUtils;
import org.omg.CORBA.FieldNameHelper;
import org.omg.CORBA.INTERNAL;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class Hello implements Serializable{


    public static void main(String[] args) throws IOException {


//        File curfile=new File("d:\\txt1.txt");
//        RandomAccessFile rw = new RandomAccessFile(curfile, "rw");
//        FileChannel channel = rw.getChannel();
//        FileOutputStream fo = new FileOutputStream("d:\\txt.txt");
//        fo.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
//        fo.close();

        System.out.println("s".getBytes().length);
        char a='西';
        System.out.println(a);
        System.out.println(Character.SIZE);

        byte[] aa=new byte[3];
        for (int i = 0; i < aa.length; i++) {
            System.out.println(aa[i]);
        }
        byte a1=-1;
        System.out.println(a1);

        System.out.println("----------------------");

        StringBuilder sb=new StringBuilder();
        System.out.println(sb.toString().equals(""));

        String teststr="(b（示范法各嘎多个。集合";
        boolean b = teststr.matches("^[\\(,（].*");
        System.out.println(b);


    }
}
