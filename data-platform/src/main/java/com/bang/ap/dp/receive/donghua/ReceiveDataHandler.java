package com.bang.ap.dp.receive.donghua;

import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.service.MonitorDataService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@ChannelHandler.Sharable
@Slf4j
public class ReceiveDataHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int timesCount = 0;

    @Autowired
    MonitorDataService monitorDataService;

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
                log.info("DH:当前采样频率：" + buf.readFloatLE());
                break;
            // 接收采样数据【时间序列数据】
            case 124:
                log.debug("收到采样数据，时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ", 位置：" + buf.readLongLE());
                int dataCount = buf.readIntLE();
                // 数据量表示一秒钟内的采样频率，如10.0Hz 则表示一秒钟采样10次。该值就是10
                log.debug("数据量：" + dataCount);
                int channelCount = buf.readIntLE();
                log.debug("通道数：" + channelCount);
                timesCount++;
                if (timesCount % 60 == 0) {
                    //声压
                    float press1 = 0;
                    float press2 = 0;
                    //震动
                    float wave1 = 0;
                    float wave2 = 0;

                    for (int i = 0; i < channelCount; i++) {
                        log.info("通道：" + (i + 1) + ", 数值：" + buf.readFloatLE());
                        if (i == 0) {
                            press1 = buf.readFloatLE();
                        }
                        if (i == 1) {
                            press2 = buf.readFloatLE();
                        }
                        if (i == 2) {
                            wave1 = buf.readFloatLE();
                        }
                        if (i == 3) {
                            wave2 = buf.readFloatLE();
                        }

                    }
                    float press = (press1 + press2) / 2;
                    float wave = (wave1 + wave2) / 2;
                    this.saveDHMonitorInfo(press,wave);
                }
                if (timesCount > 60000) {
                    timesCount = 0;
                }
                log.info(timesCount + "");
        }
    }

    private  void saveDHMonitorInfo(float press,float wave){
        MonitorData pressMonirot=new MonitorData();
        pressMonirot.setCode("DH-PRESS");
        pressMonirot.setName("声压");
        pressMonirot.setMonitorType("voicePress");
        pressMonirot.setValue(String.valueOf(press));
        pressMonirot.setOriginDateTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
        pressMonirot.setCreateTime(new Date());
        pressMonirot.setUpdateTime(new Date());
        monitorDataService.addMonitorData(pressMonirot);

        MonitorData waveMonitor=new MonitorData();
        waveMonitor.setCode("DH-WAVE");
        waveMonitor.setName("声压");
        waveMonitor.setMonitorType("voicePress");
        waveMonitor.setValue(String.valueOf(press));
        waveMonitor.setOriginDateTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
        waveMonitor.setCreateTime(new Date());
        waveMonitor.setUpdateTime(new Date());
        monitorDataService.addMonitorData(pressMonirot);

    }
}
