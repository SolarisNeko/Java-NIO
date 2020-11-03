package com.neko.Buffer;

import java.nio.ByteBuffer;

/**
 * @author SolarisNeko 11/3/2020
 */
public class demo3_DirectBuffer {
    public static void main(String[] args) {
        // 1、分配【直接缓冲区】
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

        // 2、判断是否为【直接缓冲区】
        System.out.println(byteBuffer.isDirect());

    }
}
