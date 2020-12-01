package com.bang.ap.dp.message.service.impl;

import com.bang.ap.dp.message.service.ISMSSendService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;;import java.util.ArrayList;

@Service
@Slf4j
public class TencentSMSSendService implements ISMSSendService {

    @Value("${ap.sms.secretId}")
    private String secretId;

    @Value("${ap.sms.secretKey}")
    private String secretKey;

    @Value("${ap.sms.endPoint}")
    private String endPoint;

    @Value("${ap.sms.templateId}")
    private String templateId;

    @Value("${ap.sms.sign}")
    private String sign;

    @Value("${ap.sms.sdkAppid}")
    private String sdkAppid;

    @Override
    public void sendSMS(String phoneNum, String [] templateParamSet){
        try{

            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(endPoint);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet = {"+86"+phoneNum};
            req.setPhoneNumberSet(phoneNumberSet);

            req.setTemplateParamSet(templateParamSet);

            req.setTemplateID(templateId);
            String decodedSign=new String(sign.getBytes("ISO8859-1"), "UTF-8");
            req.setSign(decodedSign);
            req.setSmsSdkAppid(sdkAppid);

            SendSmsResponse resp = client.SendSms(req);

            log.info(SendSmsResponse.toJsonString(resp));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

    }


}
