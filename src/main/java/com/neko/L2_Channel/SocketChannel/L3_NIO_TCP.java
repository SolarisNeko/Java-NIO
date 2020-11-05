package com.neko.L2_Channel.SocketChannel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 三、NIO の 使用（工作重点）
 *
 * NIO Channel 的【主要实现类】
 *
 *   java.nio.channels.Channel 接口:
 *      |-- FileChannel : 操作本地文件传输。
 *      |-- SocketChannel : 用于 TCP Client
 *      |-- ServerSocketChannel : 用于 TCP Server
 *      |-- DatagramChannel : 用于 UDP
 *
 * Channel 注册到 Selector, 会返回一个【令牌 SelectionKey】
 *
 * @author SolarisNeko 11/5/2020
 */
public class L3_NIO_TCP {

    @Test
    public void client() throws IOException {

        // 1、开启 Socket Channel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));
        // 2、将 Socket Channel 切换成 NIO
        socketChannel.configureBlocking(false);

        // 3、分配 Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 4、输入 data + 存入 Buffer, 让 Channel 写出 buffer
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入你要发送的内容: ");
        while (scanner.hasNext()) {

            String next = scanner.next();
            // 5、Channel 写出 Buffer
            buffer.put( (new Date().toString() + "\n" + next).getBytes());

            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();

            System.out.println("输入你要发送的内容: ");
        }

        // Close
        socketChannel.close();

    }

    @Test
    public void server() throws IOException {

        // 1、打开 Server Socket Channel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        // 2、配置阻塞 - 非阻塞
        ssChannel.configureBlocking(false);
        // 3、绑定 port & 接收 Client
        ssChannel.bind(new InetSocketAddress(6666));


        // 4、选择器 Selector.打开
        Selector selector = Selector.open();
        // 5、ServerSocketChannel 注册到 selector, 指定【注册事件 - 接收】
        ssChannel.register(selector, SelectionKey.OP_ACCEPT); // return SelectionKey【令牌】



        // 6、【已就绪】: 轮询 selector 中【已就绪 I/O操作】的【数量】
        while (selector.select() > 0) {

            // 7、【已注册】: 获取 Selector 中 已挑选的Key, return 集合<令牌>
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while( iterator.hasNext()) { // 已就绪 && 已注册

                // 8、获取【就绪的令牌 SelectionKey】
                SelectionKey selectionKey = iterator.next(); // 用完后, 需要取消掉 SelectionKey = 一个事件完成

                // 判断: 具体是什么 Event 准备就绪
                // 9-1、如果 令牌（SelectionKey） 是 【Acceptable 就绪】（接收 Client）
                if (selectionKey.isAcceptable()) {

                    // 1、若【接收就绪】, 获取 Client 连接
                    SocketChannel socketChannel = ssChannel.accept();

                    // 2、将 Client Socket 切换成 NIO
                    socketChannel.configureBlocking(false);

                    // 3、将 Channel 注册到 Selector 上
                    socketChannel.register(selector, SelectionKey.OP_READ);

                }
                // 9-2、如果 令牌（SelectionKey）是 【Readable 就绪】（读取 data）
                else if (selectionKey.isReadable()) {

                    // 1、获取 就绪令牌 的 Channel -> SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    // 2、构建 Buffer
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    // 3、输出 Client 传来的 Buffer
                    int len = 0;
                    while ((len = socketChannel.read(buffer)) > 0) { // 必须使用 .read(buffer) > 0, 不可以是 -1
                        buffer.flip();
                        System.out.println( new String( buffer.array(), 0, len));
                        buffer.clear();
                    }
                }

                // 10、 取消 SelectionKey （Iterator）
                iterator.remove();
            }
        }
    }
}
