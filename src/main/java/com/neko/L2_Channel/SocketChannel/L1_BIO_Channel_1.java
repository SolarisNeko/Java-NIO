package com.neko.L2_Channel.SocketChannel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 一、阻塞版本的 NIO
 *
 * Client:
 * 	FileChannel.read -> Buffer -> SocketChannel.write
 *
 * Server:
 * 	ServerSocketChannel.open -> SSChanel.bind(port) -> SSChannel.accept -> SocketChannel
 * 	-> SocketChannel.read -> Buffer -> FileChannel(输出位置, 权限).write
 * */
public class L1_BIO_Channel_1 {

	// Client
	@Test
	public void client() throws IOException {
		// 1、开启 Socket Channel
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));
				
		FileChannel fileChannel = FileChannel.open(Paths.get("H:\\Image_Output\\test.jpg"), StandardOpenOption.READ);
	
		// 2、分配 Buffer
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		// 3、用 FileChannel 读 + 用 SocketChannel 写
		while (fileChannel.read(buffer) != -1) {
			buffer.flip(); // 复位 position
			socketChannel.write(buffer);
			buffer.clear(); // 清空 buffer
		}
		
		// Close
		fileChannel.close();
		socketChannel.close();
		
	}

	// Server
	@Test
	public void server() throws IOException{
		
		// 1、开启 ServerSocketChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		// 2、ServerSocketChannel 绑定
		serverSocketChannel.bind(new InetSocketAddress(6666));
		
		// 3、接收 Client（监听）
		SocketChannel accept = serverSocketChannel.accept();
		
		// 4、分配 ByteBuffer
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		// 5、读取 Client 发来的 buffer
		FileChannel fileChannel = FileChannel.open(Paths.get("H:\\Image_Output\\test_copy.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

		while (accept.read(buffer) != -1) {
			buffer.flip();
			fileChannel.write(buffer); // 写入到 FileChannel（文件中）
			buffer.clear();
		}
		
		// Close
		fileChannel.close();
		accept.close();
		serverSocketChannel.close();
	}

}
