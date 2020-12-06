package com.bang.ap.dp.web.service;


import com.bang.ap.dp.web.entity.MessageReceiverInfo;

public interface MessageConfigService {

    MessageReceiverInfo getMessageReceiveConfig();

    void addMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo);

    void updateMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo);


}
