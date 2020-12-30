package com.bang.ap.dp.receive.donghua;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@ChannelHandler.Sharable
@Slf4j
public class ReceiveDataHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {

        // 0x55 0xAA 0xAA 0x55
        buf.readBytes(4);
        // 功能码
        int funCode = buf.readIntLE();
        // 报文长度
        int length = buf.readIntLE();

        switch (funCode) {
            // 获取当前采样频率
            case 104:
                System.out.println("当前采样频率：" + buf.readFloatLE());
                break;
            // 接收采样数据【时间序列数据】
            case 124:
         /*       System.out.println("收到采样数据，时间：" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                        ", 位置：" + buf.readLongLE());*/
                int dataCount = buf.readIntLE();
                // 数据量表示一秒钟内的采样频率，如10.0Hz 则表示一秒钟采样10次。该值就是10
               // System.out.println("数据量：" + dataCount);
                int channelCount = buf.readIntLE();
               // System.out.println("通道数：" + channelCount);

//                for (int j = 0; j < dataCount; j++) {
//                    System.out.println("第：" + (j + 1) + " 次采样");
                for (int i = 0; i < channelCount; i++) {
                   // System.out.println("通道：" + (i + 1) + ", 数值：" + buf.readFloatLE());
                }
//                }
        }
    }
}
