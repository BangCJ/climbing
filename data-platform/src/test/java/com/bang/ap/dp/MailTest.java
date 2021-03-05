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
        mailService.sendSimpleMail("", "主题：你好普通邮件", "内容：第一封邮件");
    }

    @Test
    public void sendmailHtml() {
        mailService.sendHtmlMail("", "主题：你好html邮件", " <img src=\"https://t7.baidu.com/it/u=1819248061,230866778&fm=193&f=GIF\">");
    }

    @Test
    public void sendMailWithAttachFromNetWork() {
        mailService.sendAttachmentsMailFromNetWork("", "主题：你好附件邮件", "内容：第一封附件邮件", "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF");
    }

}
