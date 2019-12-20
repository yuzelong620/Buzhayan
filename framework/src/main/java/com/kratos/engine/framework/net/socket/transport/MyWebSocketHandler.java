package com.kratos.engine.framework.net.socket.transport;

import com.alibaba.fastjson.JSON;
import com.kratos.engine.framework.net.socket.ChannelUtils;
import com.kratos.engine.framework.net.socket.IoSession;
import com.kratos.engine.framework.net.socket.SessionManager;
import com.kratos.engine.framework.net.socket.message.Message;
import com.kratos.engine.framework.net.socket.message.MessageFactory;
import com.kratos.engine.framework.net.socket.message.WebSocketFrame;
import com.kratos.engine.framework.net.socket.task.DefaultMessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	private static Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    @Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		Channel channel = ctx.channel();
		WebSocketFrame frame = JSON.parseObject(msg.text(), WebSocketFrame.class);
		if(frame.getId().equals("0")) {
			ctx.channel().writeAndFlush(msg.retain());
			return;
		}
		Class<?> clazz = MessageFactory.getInstance().getMessageMeta(frame.getModule(), frame.getCmd());
        Message message = null;
		if(frame.getMsg() == null) {
            try {
                message = (Message) clazz.newInstance();
            } catch (Exception e) {
                logger.error("", e);
            }
        } else {
            message = (Message) JSON.parseObject(frame.getMsg(), clazz);
        }
		IoSession session = ChannelUtils.getSessionBy(channel);
		
		DefaultMessageDispatcher.getInstance().dispatch(session, message, frame.getModule(), frame.getCmd());
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		if (!ChannelUtils.addChannelSession(ctx.channel(),
				new IoSession(ctx.channel()))) {
			ctx.channel().close();
			logger.error("Duplicate session,IP=[{}]",ChannelUtils.getIp(ctx.channel()));
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
        SessionManager.getInstance().unregisterPlayerChannel(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("exceptionCaught", cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				ctx.channel().close();
			}
		}
	}
}
