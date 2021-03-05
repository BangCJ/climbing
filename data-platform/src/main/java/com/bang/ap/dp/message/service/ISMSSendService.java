package com.bang.ap.dp.message.service;

public interface ISMSSendService {

    void sendSMS(String templateCode, String phoneNum, String[] templateParamSet);
}
