package com.bang.ap.dp.receive.donghua;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.ByteOrder;

@Component
public class StartGatherServiceRunner implements CommandLineRunner {

    private static final String IP = "121.248.106.130";
    private static final int PORT = 4008;

    @Autowired
    private ReceiveDataHandler receiveDataHandler;

    @Override
    public void run(String... args) throws Exception {
        new Thread(()-> {
            new Bootstrap().group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            // 数据包的最大长度
                            int maxFrameLength = 1024 * 1024 * 10;// 10MB
                            // 长度位的偏移量
                            int lengthFieldOffset = 8;
                            // 长度位的字节长度
                            int lengthFieldLength = 4;

                            // 按长度拆包设定
                            LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder =
                                    new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, maxFrameLength,
                                            lengthFieldOffset, lengthFieldLength, 0, 0, true);
                            ch.pipeline()
                                    .addLast(lengthFieldBasedFrameDecoder)
                                    .addLast(receiveDataHandler);
                        }
                    })
                    .connect(IP, PORT);

        }).run();
    }
}
