package com.bang.ap.dp.receive.hikvision.service;

import com.alibaba.fastjson.JSONObject;

public interface EventService {

    boolean doIntrusionWarning(JSONObject jsonObject);
}
