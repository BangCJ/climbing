package com.bang.ap.dp.web.service;


import com.bang.ap.dp.web.entity.MessageReceiverInfo;

import java.util.List;

public interface MessageConfigService {

    List<MessageReceiverInfo> getMessageReceiveConfigList();

    void addMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo);

    void updateMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo);


}
