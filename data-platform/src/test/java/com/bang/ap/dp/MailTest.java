package com.bang.ap.dp;

import com.bang.ap.dp.message.service.IMailSendService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
public class MailTest {
    @Autowired
    private IMailSendService mailService;

    @Test
    public void sendmail() {
        mailService.sendSimpleMail("","主题：你好普通邮件","内容：第一封邮件");
    }

    @Test
    public void sendmailHtml(){
        mailService.sendHtmlMail("","主题：你好html邮件","<h1>内容：第一封html邮件</h1>");
    }
}
