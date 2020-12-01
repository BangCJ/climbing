package com.bang.ap.dp.message.service;

public interface ISMSSendService {

    void sendSMS(String phoneNum, String [] templateParamSet);
}
