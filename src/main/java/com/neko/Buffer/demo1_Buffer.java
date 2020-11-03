package com.neko.Buffer;

import java.nio.ByteBuffer;

/**
 * 1、Buffer - 缓冲区
 *
 * 在 Java NIO 中, 负责【data 存储】. Buffer = Array[], 用于存储【不同 type 的 data】.
 * 根据 type 的不同（boolean 除外）, Java 提供了相应类型的 Buffer
 * 例如：
 *  1、ByteBuffer
 *  2、CharBuffer
 *  3、ShortBuffer
 *  4、IntBuffer
 *  5、LongBuffer
 *  6、FloatBuffer
 *  7、DoubleBuffer
 *
 * 上述 *Buffer 的管理方式几乎一致, 都是通过 *Buffer.allocate() 获取 Buffer
 *
 * 2、Buffer 存取 data 的 2个核心方法:
 *  - put(T data) : 存入 data 到 Buffer 中
 *  - get() : 从 Buffer 中 获取 data
 *
 * 3、Buffer 读写模式
 *  - 写模式（default）
 *  - 读模式 : 使用 *Buffer.flip() 进行切换（实际上将 position 刷新到 0）
 * */
public class demo1_Buffer {
    public static void main(String[] args) {
        // 1、分配一个【指定大小的 Buffer】
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024); // 1024 Byte

        System.out.println("-------------- .allocate(int size) ----------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());


        // 2、利用 .put() 存入 data
        String data = "Hello";
        byteBuffer.put(data.getBytes());

        System.out.println("-------------- .put(T[] data) 后 ----------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 3、切换成【读取 data 模式】
        byteBuffer.flip();

        System.out.println("-------------- .flip() 后 ----------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());


        // 4、缓存读 = 利用 .get(T[] data) 获取 Buffer 中的 data
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        System.out.println("-------------- .get(T[] data) 后 ----------------");
        System.out.println(new String(bytes, 0, bytes.length));

        // 5、.rewind() : 可重复读
        byteBuffer.rewind();

        System.out.println("-------------- .rewind() 后 ----------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        // 6、.clear() : 惰性清空 Buffer, 实际上 data 没有被删除, 只是标记了【没用】=="被遗忘"
        byteBuffer.clear();

        System.out.println("-------------- .clear() 后 ----------------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        System.out.println((char) byteBuffer.get()); // 还是能获取到 data

        // 7、Field mark + .reset() 
        ByteBuffer byteBuffer_mark = ByteBuffer.allocate(1024);

        String data_test = "abcsdf";
        byteBuffer_mark.put(data_test.getBytes());

        byteBuffer_mark.flip(); // 切换成【读模式】
        byte[] dst = new byte[byteBuffer_mark.limit()];
        byteBuffer_mark.get(dst, 0, dst.length);
        System.out.println(new String(dst, 0, dst.length));

        // .mark() 标记位置
        byteBuffer_mark.mark();

        byteBuffer_mark.get(dst, 2, 2);
        System.out.println(new String(dst, 2,2));
        System.out.println(byteBuffer_mark.position());

        // .reset() : 恢复到 mark 位置
        byteBuffer_mark.reset();
        System.out.println(byteBuffer_mark.position());

    }
}
