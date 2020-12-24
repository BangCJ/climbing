package com.bang.ap.dp.utils;

import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HikvisionUtil {

    @Value("${hikvision.artemisConfig.host:121.248.106.152:443}")
    private String host;

    @Value("${hikvision.artemisConfig.appKey:29878310}")
    private  String appKey;

    @Value("${hikvision.artemisConfig.appSecret:CapXj8aphSlJSMKWOUD2}")
    private String appSecret;

    @Value("${hikvision.artemis.path:/artemis}")
    private String path;

    public  String getDataFromHikvision(String url,JSONObject paramJSONObject) {
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        // artemis网关服务器ip端口
        ArtemisConfig.host = "121.248.106.152:443";
        // 秘钥appkey
        ArtemisConfig.appKey = "29878310";
        // 秘钥appSecret
        ArtemisConfig.appSecret = "CapXj8aphSlJSMKWOUD2";
        /**
         * STEP2：设置OpenAPI接口的上下文
         */
        final String ARTEMIS_PATH = "/artemis";

        /**
         * STEP3：设置接口的URI地址
         */
        final String previewURLsApi = ARTEMIS_PATH + url;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        String body = paramJSONObject.toJSONString();
        /**
         * STEP6：调用接口
         */
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        log.debug("get data from Hikvision ,url={},parma={}, result={}",url,paramJSONObject.toJSONString(),result);
        return result;
    }

}
