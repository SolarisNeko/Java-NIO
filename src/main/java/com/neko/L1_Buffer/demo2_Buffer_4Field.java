package com.neko.L1_Buffer;

import java.nio.ByteBuffer;

/**
 * @author SolarisNeko 11/3/2020
 */
public class demo2_Buffer_4Field {
    public static void main(String[] args) {
        // 7、Field mark  &  .reset()  &  .remaining()
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        String data_test = "abcdef";
        byteBuffer.put(data_test.getBytes());

        byteBuffer.flip(); // 切换成【读模式】
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst, 0, 2);
        System.out.println(new String(dst, 0, dst.length));

        // 7-1、 .mark() 标记位置
        byteBuffer.mark();

        byteBuffer.get(dst, 2, 2);
        System.out.println(new String(dst, 2,2));
        System.out.println(byteBuffer.position());

        // 7-2、 .reset() : 恢复到 mark 位置
        byteBuffer.reset();
        System.out.println(byteBuffer.position());

        // 7-3、 判断 L1_Buffer 中, 是否还有【剩余数据】
        if (byteBuffer.hasRemaining()) {
            // 获取 L1_Buffer 中【可以操作的 data 的数量】
            System.out.println(byteBuffer.remaining());
        }
    }
}
