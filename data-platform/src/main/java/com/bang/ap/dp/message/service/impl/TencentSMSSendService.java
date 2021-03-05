package com.bang.ap.dp.message.service.impl;

import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.message.service.ISMSSendService;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.web.entity.MessageLimitInfo;
import com.bang.ap.dp.web.mapper.MessageLimitMapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

;

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

    @Value("${ap.sms.enable:false}")
    private boolean isSmsEnable;

    @Value("${ap.sms.limit:10}")
    private int smsLimit;

    @Autowired
    private MessageLimitMapper messageLimitMapper;


    @Override
    public synchronized void sendSMS(String templateCode, String phoneNum, String[] templateParamSet) {
        try {
            if (!isSmsEnable) {
                log.error("短信服务未启用");
                return;
            }
            MessageLimitInfo messageLimitInfo = messageLimitMapper.getMessageLimitByDateAndType(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT_DATETYPE), "sms");
            if (messageLimitInfo != null || messageLimitInfo.getNum() > smsLimit) {
                log.error("今天{}短信发送数量超过标准{}，本条短信不做发送处理。", DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT_DATETYPE), smsLimit);
                return;
            }


            /*短信发送开始*/
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(endPoint);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet = {"+86" + phoneNum};
            req.setPhoneNumberSet(phoneNumberSet);

            req.setTemplateParamSet(templateParamSet);

            req.setTemplateID(templateCode);
            String decodedSign = new String(sign.getBytes("ISO8859-1"), "UTF-8");
            req.setSign(decodedSign);
            req.setSmsSdkAppid(sdkAppid);

            SendSmsResponse resp = client.SendSms(req);
            log.info("成功发送短信：to " + phoneNum + "  response=" + SendSmsResponse.toJsonString(resp));
            /*短信发送结束*/


            if (null == messageLimitInfo || messageLimitInfo.getNum() < 1) {
                MessageLimitInfo mli = new MessageLimitInfo();
                mli.setNum(1);
                mli.setType("sms");
                mli.setDate(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT_DATETYPE));
                mli.setCreateTime(new Date());
                mli.setUpdateTime(new Date());
                messageLimitMapper.addMessageLimit(new MessageLimitInfo());
            } else {
                messageLimitMapper.updateNumByDateAndType(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT_DATETYPE), "sms", messageLimitInfo.getNum() + 1);
            }
        } catch (Exception e) {
            log.error("短信发送失败", e);
        }

    }


}
