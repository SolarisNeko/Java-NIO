package com.neko.L2_Channel;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 1、L2_Channel（通道）介绍：
 *  1-1、用于【源节点】与【目标节点】的连接。
 *  1-2、在 Java NIO 中, 负责 L1_Buffer 中 data 的传输。
 *
 * ps：
 *  L2_Channel 本身不存储 data, 因此需要配合 L1_Buffer 进行传输。
 *
 * 2、L2_Channel 的【主要实现类】
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
public class demo3_Channel_MappedByteBuffer {
    public static void main(String[] args) throws IOException {
/**
 * 3、内存映射文件（映射缓存）
 *
 *  Paths -> since JDK 1.7 - java.nio.file
 * 问题:
 *  File copy 完, 但【程序】可能【没有结束】, JVM 可能持续占用资源
 *   即：虽然效率高, 但不够稳定！！
 * */
        long start = System.currentTimeMillis();

        // 1、构建【文件通道】
        FileChannel input_Channel = FileChannel.open(Paths.get("H:\\", "Image_Output\\test.jpg"), StandardOpenOption.READ);
        FileChannel output_Channel = FileChannel.open(Paths.get("H:\\", "Image_Output\\", "test_copy.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // 2、映射缓存 【length = inputChannel.size() 】
        MappedByteBuffer inputMappedBuffer = input_Channel.map(FileChannel.MapMode.READ_ONLY, 0, input_Channel.size());
        MappedByteBuffer outputMappedBuffer = output_Channel.map(FileChannel.MapMode.READ_WRITE, 0, input_Channel.size());

        // 3、直接对 L1_Buffer 进行 data 的 读写操作（因为是【物理内存】)
        byte[] dst = new byte[inputMappedBuffer.limit()];

        inputMappedBuffer.get(dst);
        outputMappedBuffer.put(dst);

        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));

        // Close
        input_Channel.close();
        output_Channel.close();

    }


}
