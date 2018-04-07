package com.weyong.onenet.server.handler;

import com.weyong.onenet.server.context.OneNetServerContext;
import com.weyong.onenet.server.session.OneNetSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by hao.li on 2017/4/12.
 */
@Slf4j
public class InternetChannelInitializer extends ChannelInitializer<SocketChannel> {
    private OneNetServerContext oneNetServerContext;

    public InternetChannelInitializer(OneNetServerContext oneNetServerContext){
        this.oneNetServerContext = oneNetServerContext;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        Channel oneNetChannel =
                oneNetServerContext.getOneNetServer().getOneNetConnectionManager().getAvailableChannel(
                        oneNetServerContext.getOneNetServerContextConfig().getContextName());
        if(oneNetChannel == null){
            ch.close();
        }else {
            OneNetSession oneNetSession =oneNetServerContext.createSession(ch ,oneNetChannel);
            oneNetSession.setOneNetServerContext(oneNetServerContext);
            ch.pipeline()
                    .addLast(new InternetChannelInboundHandler(oneNetSession))
                    .addLast(new ByteArrayEncoder());
        }
    }
}