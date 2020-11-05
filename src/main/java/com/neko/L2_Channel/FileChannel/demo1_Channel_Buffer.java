package com.neko.L2_Channel.FileChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1、L2_Channel（通道）介绍：
 *  1-1、用于【源节点】与【目标节点】的连接。
 *  1-2、在 Java NIO 中, 负责 L1_Buffer 中 data 的传输。
 *
 * ps：
 *  L2_Channel 本身不存储 data, 因此需要配合 L1_Buffer 进行传输。
 *
 * 2、NIO Channel 的【主要实现类】
 *   java.nio.channels.L2_Channel 接口:
 *      |-- FileChannel : 操作本地文件传输。
 *      |-- SocketChannel : 用于 TCP Client
 *      |-- ServerSocketChannel : 用于 TCP Server
 *      |-- DatagramChannel : 用于 UDP
 *
 * 3、获取 L2_Channel
 *   3-1、Java 针对【支持 L2_Channel 的类】提供了 getChannel() 方法
 *      本地 I/O:
 *         FileInputStream / FileOutputStream
 *         RandomAccessFile
 *      网络 I/O:
 *          Socket
 *          ServerSocket
 *          DatagramSocket
 *
 *  3-2、在 JDK 1.7 中的 NIO.2 针对各个 L2_Channel 提供了 static method - open()
 *
 *  3-3、在 JDK 1.7 中的 NIO.2 的【Files 工具类】的 .newByteChannel()
 *
 *
 * @author SolarisNeko 11/3/2020
 */
public class demo1_Channel_Buffer {
    public static void main(String[] args) throws IOException {
        /**
         * 1、利用 L2_Channel 完成 File 的 copy (JVM Memory - 非直接缓冲区）
         *
         *  File -> FileInputStream -> FileChannel(read) -> 【ByteBuffer】 -> FileChannel（write) -> FileOutputStream -> File
         * */
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        FileChannel input_Channel = null;
        FileChannel output_Channel = null;

        try {
            long start = System.currentTimeMillis();

            // 输入点
            File source_file = new File("H:\\Image_Output\\test.jpg");
            fileInputStream = new FileInputStream(source_file);

            // 输出点
            File copy_file = new File("H:\\Image_Output\\test_copy.jpg");
            fileOutputStream = new FileOutputStream(copy_file);

            // 1、通过 I/O Stream -> 获取 I/O L2_Channel
            input_Channel = fileInputStream.getChannel();
            output_Channel = fileOutputStream.getChannel();

            // 2、获取 data - L1_Buffer
            // 2-1、分配指定大小的 L1_Buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            // 3、将 L2_Channel 中的 data 存入 L1_Buffer 中 ->  Chanel.read(L1_Buffer buffer)
            while (input_Channel.read(byteBuffer) != -1) {
                // 3-1、L1_Buffer 切换成 读模式 -> .flip()
                byteBuffer.flip();

                // 3=2、将 L1_Buffer 中的 data 写入到 L2_Channel -> Chanel.write(L1_Buffer buffer)
                output_Channel.write(byteBuffer);

                // 3-3、清空 L1_Buffer
                byteBuffer.clear();
            }

            long end = System.currentTimeMillis();
            System.out.println("time = " + (end - start));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close
            try {
                if (output_Channel != null) {
                    output_Channel.close();
                }

                if (input_Channel != null) {
                    input_Channel.close();
                }

                fileOutputStream.close();
                fileInputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
