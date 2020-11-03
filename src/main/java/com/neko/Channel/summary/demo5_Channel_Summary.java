//package com.neko.Channel;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.MappedByteBuffer;
//import java.nio.channels.FileChannel;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//
///**
// * 1、Channel（通道）介绍：
// *  1-1、用于【源节点】与【目标节点】的连接。
// *  1-2、在 Java NIO 中, 负责 Buffer 中 data 的传输。
// *
// * ps：
// *  Channel 本身不存储 data, 因此需要配合 Buffer 进行传输。
// *
// * 2、Channel 的【主要实现类】
// *   java.nio.channels.Channel 接口:
// *      |-- FileChannel : 操作本地文件传输。
// *      |-- SocketChannel : 用于 TCP Client
// *      |-- ServerSocketChannel : 用于 TCP Server
// *      |-- DatagramChannel : 用于 UDP
// *
// * 3、获取 Channel
// *   3-1、Java 针对【支持 Channel 的类】提供了 getChannel() 方法
// *      本地 I/O:
// *         FileInputStream / FileOutputStream
// *         RandomAccessFile
// *      网络 I/O:
// *          Socket
// *          ServerSocket
// *          DatagramSocket
// *
// *  3-2、在 JDK 1.7 中的 NIO.2 针对各个 Channel 提供了 static method - open()
// *
// *  3-3、在 JDK 1.7 中的 NIO.2 的【Files 工具类】的 .newByteChannel()
// *
// *
// * @author SolarisNeko 11/3/2020
// */
//public class demo5_Channel_Summary {
//    public static void main(String[] args) throws IOException {
//        // 1、利用 Channel 完成 File 的 copy（非直接缓冲区）
////        Channel_File_Copy_allocate();
//
//        // 2、利用 Channel 完成 File 的 copy（直接缓冲区）
////        Channel_File_Copy_allocateDirect();
//
//        // 3、内存映射文件（映射缓存）
////        Channel_Memory();
//
//        // 4、Channel 之间的 data 传输 (直接缓冲区）
//        Channel_Transfer();
//    }
//
//    /**
//     * 1、利用 Channel 完成 File 的 copy (JVM Memory - 非直接缓冲区）
//     *
//     *  File -> FileInputStream -> FileChannel(read) -> 【ByteBuffer】 -> FileChannel（write) -> FileOutputStream -> File
//     * */
//    public static void Channel_File_Copy_allocate()  {
//
//        FileInputStream fileInputStream = null;
//        FileOutputStream fileOutputStream = null;
//
//        FileChannel input_Channel = null;
//        FileChannel output_Channel = null;
//
//        try {
//            long start = System.currentTimeMillis();
//
//            // 输入点
//            File source_file = new File("H:\\Image_Output\\test.jpg");
//            fileInputStream = new FileInputStream(source_file);
//
//            // 输出点
//            File copy_file = new File("H:\\Image_Output\\test_copy.jpg");
//            fileOutputStream = new FileOutputStream(copy_file);
//
//            // 1、通过 I/O Stream -> 获取 I/O Channel
//            input_Channel = fileInputStream.getChannel();
//            output_Channel = fileOutputStream.getChannel();
//
//            // 2、获取 data - Buffer
//            // 2-1、分配指定大小的 Buffer
//            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//
//            // 3、将 Channel 中的 data 存入 Buffer 中 ->  Chanel.read(Buffer buffer)
//            while (input_Channel.read(byteBuffer) != -1) {
//                // 3-1、Buffer 切换成 读模式 -> .flip()
//                byteBuffer.flip();
//
//                // 3=2、将 Buffer 中的 data 写入到 Channel -> Chanel.write(Buffer buffer)
//                output_Channel.write(byteBuffer);
//
//                // 3-3、清空 Buffer
//                byteBuffer.clear();
//            }
//
//            long end = System.currentTimeMillis();
//            System.out.println("time = " + (end - start));
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Close
//            try {
//                if (output_Channel != null) {
//                    output_Channel.close();
//                }
//
//                if (input_Channel != null) {
//                    input_Channel.close();
//                }
//
//                fileOutputStream.close();
//                fileInputStream.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//
//    /**
//     * 2、利用 Channel 完成 File 的 copy （OS Memory - 直接缓冲区)
//     *
//     * 内存映射文件, 效率很高！
//     * ps：【直接缓冲区】, 只有 ByteBuffer 支持。
//     * */
//    public static void Channel_File_Copy_allocateDirect()  {
//
//        long start = System.currentTimeMillis();
//
//        FileInputStream fileInputStream = null;
//        FileOutputStream fileOutputStream = null;
//
//        FileChannel input_Channel = null;
//        FileChannel output_Channel = null;
//
//        try {
//            // 输入点
//            File source_file = new File("H:\\Image_Output\\test.jpg");
//            fileInputStream = new FileInputStream(source_file);
//
//            // 输出点
//            File copy_file = new File("H:\\Image_Output\\test_copy.jpg");
//            fileOutputStream = new FileOutputStream(copy_file);
//
//            // 1、通过 I/O Stream -> 获取 I/O Channel
//            input_Channel = fileInputStream.getChannel();
//            output_Channel = fileOutputStream.getChannel();
//
//            // 2、获取 data - Buffer
//            // 2-1、分配指定大小的 Buffer
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
//
//            // 3、将 Channel 中的 data 存入 Buffer 中 ->  Chanel.read(Buffer buffer)
//            while (input_Channel.read(byteBuffer) != -1) {
//                // 3-1、Buffer 切换成 读模式 -> .flip()
//                byteBuffer.flip();
//
//                // 3=2、将 Buffer 中的 data 写入到 Channel -> Chanel.write(Buffer buffer)
//                output_Channel.write(byteBuffer);
//
//                // 3-3、清空 Buffer
//                byteBuffer.clear();
//            }
//
//            long end = System.currentTimeMillis();
//            System.out.println("time = " + (end - start));
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Close
//            try {
//                if (output_Channel != null) {
//                    output_Channel.close();
//                }
//
//                if (input_Channel != null) {
//                    input_Channel.close();
//                }
//
//                fileOutputStream.close();
//                fileInputStream.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    /**
//     * 3、内存映射文件（映射缓存）
//     *
//     *  Paths -> since JDK 1.7 - java.nio.file
//     * 问题:
//     *  File copy 完, 但【程序】可能【没有结束】, JVM 可能持续占用资源
//     *   即：虽然效率高, 但不够稳定！！
//     * */
//    public static void Channel_Memory() throws IOException {
//        long start = System.currentTimeMillis();
//
//        // 1、构建【文件通道】
//        FileChannel input_Channel = FileChannel.open(Paths.get("H:\\", "Image_Output\\test.jpg"), StandardOpenOption.READ);
//        FileChannel output_Channel = FileChannel.open(Paths.get("H:\\", "Image_Output\\", "test_copy.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
//
//        // 2、映射缓存 【length = inputChannel.size() 】
//        MappedByteBuffer inputMappedBuffer = input_Channel.map(FileChannel.MapMode.READ_ONLY, 0, input_Channel.size());
//        MappedByteBuffer outputMappedBuffer = output_Channel.map(FileChannel.MapMode.READ_WRITE, 0, input_Channel.size());
//
//        // 3、直接对 Buffer 进行 data 的 读写操作（因为是【物理内存】)
//        byte[] dst = new byte[inputMappedBuffer.limit()];
//
//        inputMappedBuffer.get(dst);
//        outputMappedBuffer.put(dst);
//
//        long end = System.currentTimeMillis();
//        System.out.println("time = " + (end - start));
//
//        // Close
//        input_Channel.close();
//        output_Channel.close();
//
//    }
//
//    // 4、Channel 之间的 data 传输 (直接缓冲区）
//    public static void Channel_Transfer() throws IOException {
//        // 1、构建 I/O Channel
//        FileChannel inputChannel = FileChannel.open(Paths.get("H:\\", "Image_Output\\test.jpg"), StandardOpenOption.READ);
//        FileChannel outputChannel = FileChannel.open(Paths.get("H:\\", "Image_Output\\test_copy.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
//
//        // 2、传输到( 起始位, 大小, 输出的Channel )
//        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
////        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
//
//        inputChannel.close();
//        outputChannel.close();
//
//
//    }
//}
