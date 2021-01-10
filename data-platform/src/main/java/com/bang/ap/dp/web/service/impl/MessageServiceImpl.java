package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.message.service.IMailSendService;
import com.bang.ap.dp.message.service.ISMSSendService;
import com.bang.ap.dp.web.entity.MessageInfo;
import com.bang.ap.dp.web.entity.MessageReceiverInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.entity.WarningInfo;
import com.bang.ap.dp.web.mapper.MessageMapper;
import com.bang.ap.dp.web.mapper.UserMapper;
import com.bang.ap.dp.web.service.MessageConfigService;
import com.bang.ap.dp.web.service.MessageService;
import com.bang.ap.dp.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
        if (!isMessgeEnable){
            log.error("当前环境配置不发送消息");
            return ;
        }
        //查询消息接收配置
        List<MessageReceiverInfo> messageReceiverInfoList= messageConfigService.getMessageReceiveConfigList();
        if (messageReceiverInfoList!=null && messageReceiverInfoList.size()>0){
            for (int i = 0; i <messageReceiverInfoList.size() ; i++) {
                String receiverId=messageReceiverInfoList.get(i).getReceiverId();
                //查询用户信息
                UserInfo userInfo=userService.getUserById(Integer.valueOf(receiverId));
                if(null!=userInfo){
                    String emialAddress=userInfo.getEmail();
                    String phone=userInfo.getPhone();

                    //消息发送
                    if (messageReceiverInfoList.get(i).isEmailEnable()&&StringUtils.isNotEmpty(emialAddress)){
                        iMailSendService.sendSimpleMail(emialAddress,"实验室预警消息",warning.getWarningArea()+warning.getWarningContent());
                    }
                    if (messageReceiverInfoList.get(i).isSmsEnable()&& StringUtils.isNotEmpty(phone)){
                        String area=warning.getWarningArea().replace("实验室","");
                        String content="";
                        if (warning.getWarningContent().contains("温度")){
                            content="温度超过阈值";
                        }
                        ismsSendService.sendSMS(phone,new String[]{area, content});
                    }
                }

            }
        }


    }

}
