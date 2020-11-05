package com.neko.L2_Channel.FileChannel;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**  字符集: Charset
 *
 * 1、编码 Coding = String -> Byte[]
 * 2、解码 Decode = Byte[] -> String
 *
 * @author SolarisNeko 11/4/2020
 */
public class demo6_Charset_Encoder_Decoder {
    public static void main(String[] args) throws CharacterCodingException {
        // 0、输出【可用的 Charset】
//        availiableCharset();

        // 1、【反射】获取【编码】
        Charset charset = Charset.forName("GBK");
        // 2、获取【编码器】
        CharsetEncoder encoder = charset.newEncoder();
        // 3、获取【解码器】
        CharsetDecoder decoder = charset.newDecoder();


        // 4、【构建 Buffer】
        CharBuffer buffer = CharBuffer.allocate(1024);
        // 5、将 data 放入 Buffer
        buffer.put("Hello Neko~");
        buffer.flip();

        // 6-1、【编码】 = CharBuffer -> ByteBuffer
        ByteBuffer encodeBuffer = encoder.encode(buffer);
        // 6-2、查看【编码】效果
        for (int i = 0; i < 11; i++) {
            System.out.println(encodeBuffer.get()); // 输出 ASCII Code
        }

        // 7、【解码】 = ByteBuffer -> CharBuffer
        encodeBuffer.flip(); // 重置 position
        CharBuffer decode = decoder.decode(encodeBuffer);
        // 7-2、查看【解码】效果
        System.out.println(decode.toString());

        /**
         * 8、模拟【非对称 编码/解码】
         *  GBK -> CharBuffer -> GBK CharsetEncoder.encode() -> 【ByteBuffer】 -> UTF-8 CharsetDecoder.decode() -> CharBuffer
         *
         * ps: 非对称 编码/解码 -> 会导致【出现乱码】
         * */
        Charset charset_utf8 = Charset.forName("UTF-8");
        CharsetDecoder utf8Decoder = charset_utf8.newDecoder();

        // 8-2、重置 ByteBuffer.position & 【解码】
        encodeBuffer.flip();
        CharBuffer decode2 = utf8Decoder.decode(encodeBuffer);
        System.out.println(decode2.toString());

    }

    public static void availiableCharset() {
        // 1、获取【可用的 Charset】
        SortedMap<String, Charset> sortedMap = Charset.availableCharsets();
        // 2、获取【不重复个体集合 entrySet】
        Set<Map.Entry<String, Charset>> entries = sortedMap.entrySet();

        for (Map.Entry<String, Charset> entry : entries) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
