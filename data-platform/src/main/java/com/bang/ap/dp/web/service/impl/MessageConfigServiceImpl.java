package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.utils.ValidateUtil;
import com.bang.ap.dp.web.entity.MessageReceiverInfo;
import com.bang.ap.dp.web.entity.PwdInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.MessageConfigMapper;
import com.bang.ap.dp.web.mapper.PwdMapper;
import com.bang.ap.dp.web.mapper.UserMapper;
import com.bang.ap.dp.web.service.MessageConfigService;
import com.bang.ap.dp.web.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class MessageConfigServiceImpl implements MessageConfigService {

    @Autowired
    private MessageConfigMapper messageConfigMapper;

    @Override
    public MessageReceiverInfo getMessageReceiveConfig() {
       List<MessageReceiverInfo>  messageReceiverInfoList= messageConfigMapper.getMessageReceiveConfigList();
       if (messageReceiverInfoList!=null&&messageReceiverInfoList.size()>0){
           return messageReceiverInfoList.get(0);
       }
        return new MessageReceiverInfo();
    }

    @Override
    public void addMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo) {
        messageConfigMapper.addMessageReceiveConfig(messageReceiverInfo);

    }

    @Override
    public void updateMessageReceiveConfig(MessageReceiverInfo messageReceiverInfo) {
        messageConfigMapper.updateMessageReceiveConfig(messageReceiverInfo);

    }
}
