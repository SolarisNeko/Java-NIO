package com.neko.L3_Pipe;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 需要记的英文:
 *  sink  n.  水槽,巢
 *
 * @author SolarisNeko 11/5/2020
 */
public class L1_Pipe_Demo {

    @Test
    public void pipe_demo() throws IOException {
        // 1、打开管道 Pipe
        Pipe pipe = Pipe.open();

        // 2、将 Buffer 写到 Channel
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Pipe.SinkChannel sinkChannel = pipe.sink();

        buffer.put("通过 单向管道(Pipe) 发送数据".getBytes());
        buffer.flip();
        sinkChannel.write(buffer);

        // 3、读取 Buffer 中的 data
        Pipe.SourceChannel sourceChannel = pipe.source();

        buffer.flip();
        int length = sourceChannel.read(buffer);
        System.out.println( new String(buffer.array(), 0, length));
        buffer.clear();

        sourceChannel.close();
        sinkChannel.close();

    }
}
