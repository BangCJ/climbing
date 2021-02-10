package com.bang.ap.dp.receive.hikvision.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.receive.hikvision.service.EventService;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.web.entity.WarningInfo;
import com.bang.ap.dp.web.mapper.WarningMapper;
import com.bang.ap.dp.web.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private WarningMapper warningMapper;

    @Autowired
    private MessageService messageService;


    @Override
    public boolean doIntrusionWarning(JSONObject jsonObject) {
        WarningInfo warningInfo = new WarningInfo();
        warningInfo.setWarningArea("实验室300");
        warningInfo.setWarningType("intrusionWarning");
        warningInfo.setWarningTime(DPTimeUtil.formatDate(DPTimeUtil.getNowDate(), DPConstant.DATE_FORMAT_DATETYPE));
        warningInfo.setCreateTime(new Date());
        warningInfo.setUpdateTime(new Date());
        warningInfo.setWarningContent("监测到区域入侵行为");
        if (StringUtils.isNotEmpty(analysisIntrusionWarning(jsonObject))){
            warningInfo.setWarningAttach(analysisIntrusionWarning(jsonObject));
        }
        warningMapper.addWarningInfo(warningInfo);
        try {
            messageService.sendMessage(warningInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    public String analysisIntrusionWarning(JSONObject jsonObject) {
        String result = "";
        JSONObject params = jsonObject.getJSONObject("params");
        if (null != params) {
            JSONObject eventObject = params.getJSONArray("events").getJSONObject(0);
            if (eventObject != null) {
                String eventType = eventObject.getString("eventType");
                String happenTime = eventObject.getString("happenTime");
                String imageUrl = eventObject.getJSONObject("data").getJSONArray("fielddetection").getJSONObject(0).getString("imageUrl");
                result = imageUrl;
            }
        }
        return result;
    }
}
