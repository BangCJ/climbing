package com.bang.ap.dp.web.service.impl;


import com.alibaba.fastjson.JSON;
import com.bang.ap.dp.message.service.IMailSendService;
import com.bang.ap.dp.message.service.ISMSSendService;
import com.bang.ap.dp.web.entity.MessageInfo;
import com.bang.ap.dp.web.entity.MessageReceiverInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.entity.WarningInfo;
import com.bang.ap.dp.web.mapper.MessageMapper;
import com.bang.ap.dp.web.service.MessageConfigService;
import com.bang.ap.dp.web.service.MessageService;
import com.bang.ap.dp.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Value("${ap.message.enable:false}")
    private boolean isMessgeEnable;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ISMSSendService ismsSendService;

    @Autowired
    private IMailSendService iMailSendService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageConfigService messageConfigService;


    @Override
    public MessageInfo getMessageById(int id) {

        MessageInfo messageInfo = messageMapper.selectById(id);

        return messageInfo;
    }

    @Override
    public void addMessageInfo(MessageInfo messageInfo) {
        messageMapper.addMessageInfo(messageInfo);
    }

    @Override
    public void sendMessage(WarningInfo warning) {
        if (!isMessgeEnable) {
            log.error("当前环境配置不发送消息，消息内容为{}", JSON.toJSONString(warning));
            return;
        }
        //查询消息接收配置
        List<MessageReceiverInfo> messageReceiverInfoList = messageConfigService.getMessageReceiveConfigList();
        if (messageReceiverInfoList != null && messageReceiverInfoList.size() > 0) {
            for (int i = 0; i < messageReceiverInfoList.size(); i++) {
                String receiverId = messageReceiverInfoList.get(i).getReceiverId();
                /*查询用户信息*/
                UserInfo userInfo = userService.getUserById(Integer.valueOf(receiverId));
                if (null != userInfo) {
                    String emialAddress = userInfo.getEmail();
                    String phone = userInfo.getPhone();

                    /*消息发送*/
                    //发送邮件
                    if (messageReceiverInfoList.get(i).isEmailEnable() && StringUtils.isNotEmpty(emialAddress)) {
                        this.sendEmailForWarningInfo(emialAddress, warning);
                    }
                    //发送短信
                    if (messageReceiverInfoList.get(i).isSmsEnable() && StringUtils.isNotEmpty(phone)) {
                        this.sendSMSForWarningInfo(phone, warning);
                    }
                } else {
                    log.error("发送消息时，根据id{}获取用户信息为空，消息发送终止。", receiverId);
                }

            }
        }
    }

    /**
     * 处理短信内容
     *
     * @param messageContent
     * @return
     */
    private String dealMessageContentForSMS(String messageContent) {
        if (StringUtils.isEmpty(messageContent)) {
            return "messageContent is empty";
        }
        if (messageContent.contains("温度")) {
            messageContent = "温度超过阈值";
        }
        return messageContent;
    }

    /**
     * 将预警信息通过短息发送
     *
     * @param phoneNum
     * @param warning
     */
    private boolean sendSMSForWarningInfo(String phoneNum, WarningInfo warning) {
        String area = warning.getWarningArea().replace("实验室", "");
        if ("intrusionWarning".equals(warning.getWarningType())) {
            ismsSendService.sendSMS("876890", phoneNum, new String[]{area});
        } else if ("sensorWarning".equals(warning.getWarningType())) {
            String content = dealMessageContentForSMS(warning.getWarningContent());
            ismsSendService.sendSMS("876911", phoneNum, new String[]{area, "", content});
        } else if ("trendWarning".equals(warning.getWarningType())) {
            ismsSendService.sendSMS("876940", phoneNum, new String[]{area, "火灾"});
        }
        return true;
    }

    /**
     * 将预警信息通过邮箱发送
     *
     * @param emailAddress
     * @param warning
     */
    private boolean sendEmailForWarningInfo(String emailAddress, WarningInfo warning) {
        try {
            if ("intrusionWarning".equals(warning.getWarningType())) {
                iMailSendService.sendAttachmentsMailFromNetWork(emailAddress, "实验室预警消息", warning.getWarningArea() + warning.getWarningContent(), warning.getWarningAttach());
            }else{
                iMailSendService.sendSimpleMail(emailAddress, "实验室预警消息", warning.getWarningArea() + warning.getWarningContent());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return true;
    }

}
