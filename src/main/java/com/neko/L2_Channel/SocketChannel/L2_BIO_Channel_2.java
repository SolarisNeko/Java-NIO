package com.neko.L2_Channel.SocketChannel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *  二、阻塞版本的 NIO - 2
 * */
public class L2_BIO_Channel_2 {

    @Test
    public void client() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));

        FileChannel fileChannel = FileChannel.open(Paths.get("H:\\Image_Output\\test.jpg"), StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (fileChannel.read(buffer) != -1) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }

        /**
         * 在 BIO 中
         * Client 必须关闭 Output Channel, 否则 thread 会一直 Blocking
         *
         * 原因:
         *    此处使用, 仍然是 BIO, I/O 只能选【其一】。
         * */
        socketChannel.shutdownOutput();

        // 读取 Server 反馈
        int length = 0;
        while ((length = socketChannel.read(buffer)) != -1) {
            buffer.flip();
            // 输出 反馈
            System.out.println(new String(buffer.array(), 0, length));
            buffer.clear();
        }

        fileChannel.close();
        socketChannel.close();

    }

    @Test
    public void server() throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel fileChannel = FileChannel.open(Paths.get("H:\\Image_Output\\test_copy.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        serverSocketChannel.bind(new InetSocketAddress(6666));
        SocketChannel socketChannel = serverSocketChannel.accept();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (socketChannel.read(buffer) != -1) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();

        }

        // 给 Client 发送 反馈
        buffer.put("Copy successfully !".getBytes());

        buffer.flip();
        socketChannel.write(buffer);


        socketChannel.close();
        fileChannel.close();
        serverSocketChannel.close();
    }
}
