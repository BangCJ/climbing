package com.bang.ap.dp;

import com.bang.ap.dp.message.service.ISMSSendService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public class SMSTest {
    @Autowired
    private ISMSSendService ismsSendService;

    @Test
    public void sendSMS() {
        ismsSendService.sendSMS("18678898329",new String[]{"301", "湿度超过阈值"});

    }

}
