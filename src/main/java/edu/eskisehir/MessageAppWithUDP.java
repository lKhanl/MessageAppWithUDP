package edu.eskisehir;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class MessageAppWithUDP {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Client());
        Thread t2 = new Thread(new Server());

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}

class Client implements Runnable {

    @Override
    public void run() {

        try (DatagramSocket ds1 = new DatagramSocket(1234)) {

            while (true) {

                byte[] receive = new byte[65535];
                DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);

                try {
                    ds1.receive(DpReceive);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (!data(receive).toString().equals("")) {
                    System.out.println("Client: " + data(receive));
                }
                if (data(receive).toString().equals("exit")) {
                    System.exit(0);
                }

            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }


    }

    public static StringBuilder data(byte[] a) {
        if (a == null || a.length == 0)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}

class Server implements Runnable {

    Scanner sc = new Scanner(System.in);

    @Override
    public void run() {

        try (DatagramSocket ds = new DatagramSocket();) {
            //change ip address to your friend's computer's ip address
//            InetAddress ip = InetAddress.getByName("192.168.1.57");
            InetAddress ip = InetAddress.getByName("localhost");
            while (true) {

                String inp = sc.nextLine();

                byte[] buf = inp.getBytes();

                DatagramPacket DpSend =
                        new DatagramPacket(buf, buf.length, ip, 1234);

                ds.send(DpSend);

                if (inp.equals("exit"))
                    System.exit(0);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}