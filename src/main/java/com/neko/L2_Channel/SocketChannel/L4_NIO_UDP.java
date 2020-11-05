package com.neko.L2_Channel.SocketChannel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author SolarisNeko 11/5/2020
 */
public class L4_NIO_UDP {

    @Test
    public void sender() throws IOException {

        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);

        System.out.println("可以开始聊天啦~\n");
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((new Date().toString() + ": \n" + next).getBytes());

            buffer.flip();
            datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1", 6666));
            buffer.clear();
            System.out.println("请输入: ");
        }

        datagramChannel.close();

    }

    @Test
    public void receiver() throws IOException {
        DatagramChannel datagramChannel = DatagramChannel.open();

        datagramChannel.configureBlocking(false);

        datagramChannel.bind(new InetSocketAddress(6666));

        Selector selector = Selector.open();
        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                if (selectionKey.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    datagramChannel.receive(buffer);

                    buffer.flip();
                    System.out.println( new String( buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }

            iterator.remove();
        }

    }
}
