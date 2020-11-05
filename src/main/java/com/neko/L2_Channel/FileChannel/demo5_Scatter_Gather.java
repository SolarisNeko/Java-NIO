package com.neko.L2_Channel.FileChannel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Scatter v. 分散
 * Gather  v. 聚集
 *
 * 分散读取（Scattering Read） : 将一个 Channel 里的 data 分散（Scatter）到多个 Buffer 中
 * 聚集写入（Gathering Write） : 将多个 Buffer 中的 data 聚集（Gather）到一个 Channel 中
 *
 * @author SolarisNeko 11/4/2020
 */
public class demo5_Scatter_Gather {
    public static void main(String[] args) throws IOException {

        // 1、指定文件 & 获取其length
        RandomAccessFile randomAccessFile = new RandomAccessFile("H:\\Image_Output\\test.jpg", "rw");
        long length = randomAccessFile.length()/2;

        // 2、获取 Channel
        FileChannel channelSource = randomAccessFile.getChannel();

        // 3、分配指定大小的 Buffer
        ByteBuffer buffer1 = ByteBuffer.allocate((int) length);
        ByteBuffer buffer2 = ByteBuffer.allocate((int) length);

        // 4、组装成 Buffer[] 容器
        ByteBuffer[] buffers = { buffer1, buffer2 };

        // 5、Scattering Read - 分散读取
        channelSource.read(buffers);

        // 6、将 容器 Buffer[] 中的每一个 Buffer 切换成【读取模式】
        for (ByteBuffer byteBuffer : buffers) {
            byteBuffer.flip();
        }

        // Test: 输出 Buffer 中 内容
//        System.out.println(new String( buffers[0].array(), 0, buffers[0].limit()));
//        System.out.println(new String( buffers[1].array(), 0, buffers[1].limit()));

        System.out.println("---------------------------------------------------------");

        // 7、聚集写入
        RandomAccessFile copy = new RandomAccessFile("H:\\Image_Output\\test_copy.jpg", "rw");
        FileChannel channelCopy = copy.getChannel();

        channelCopy.write(buffers);

    }
}
