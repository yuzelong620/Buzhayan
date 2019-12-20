package com.kratos.engine.framework.net.socket.transport;

import com.kratos.engine.framework.net.ServerNode;
import com.kratos.engine.framework.net.socket.message.MessageFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Log4j
@Component
public class SocketServer implements ServerNode {

    // 避免使用默认线程数参数
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

    private int port;
    private int heartbeatTick;

    @Override
    public void init(int port, int heartbeatTick) {
        this.port = port;
        this.heartbeatTick = heartbeatTick;

        // 初始化协议表
        MessageFactory.getInstance().init();
    }

    @Override
    public void start() throws Exception {
        log.info("socket服务已启动，正在监听用户的请求@port:" + port + "......");
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            b.bind(new InetSocketAddress(port)).sync();
        } catch (Exception e) {
            log.error("", e);

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            throw e;
        }
    }

    @Override
    public void shutDown() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) {
            ChannelPipeline pipeline = arg0.pipeline();
            // HttpServerCodec: 针对http协议进行编解码
            pipeline.addLast("httpServerCodec", new HttpServerCodec());
            // ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
            pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
            pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(8192));

            // 用于处理websocket, /ws为访问websocket时的uri
            pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"));
            pipeline.addLast("idleStateHandler", new IdleStateHandler(heartbeatTick, 0, 0, TimeUnit.SECONDS));
            pipeline.addLast("myWebSocketHandler", new MyWebSocketHandler());
        }
    }
}
