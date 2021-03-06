package com.stormdzh.mina;

import com.stormdzh.mina.socket.SocketEncoder;
import com.stormdzh.mina.handler.ClientHandler;
import com.stormdzh.mina.socket.SocketDecoder;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MinaClientTest {

    @Test
    public void mina() {
        // 创建客户端连接器.
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        // 设置编码过滤器
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new SocketEncoder(),new SocketDecoder()));
        // 设置事件处理器
        connector.setHandler(new ClientHandler());
        // 建立连接
        ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 10010));
        // 等待连接创建完成
        cf.awaitUninterruptibly();
        // 发送消息，中英文符号都有
        cf.getSession().write("hello,d的十多年弗兰克萨洛克{id:丹参粉！！！}");
        // 创建客户端连接器
        NioSocketConnector connector2 = new NioSocketConnector();
        connector2.getFilterChain().addLast("logger", new LoggingFilter());
        // 设置编码过滤器
        connector2.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
        // 设置事件处理器
        connector2.setHandler(new ClientHandler());
        // 建立连接
        ConnectFuture cf2 = connector2.connect(new InetSocketAddress("127.0.0.1", 10011));
        // 等待连接创建完成
        cf2.awaitUninterruptibly();
        // 发送消息，中英文都有
        cf2.getSession().write("test");
    }

}
