package com.bang.ap.dp.utils;

import com.alibaba.fastjson.JSONObject;

public class ResponseUtil {

    public static JSONObject buildSuccessResponse(Object data,String info){
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("status","1");
        jsonObject.put("info",info);
        jsonObject.put("data",data);
        return jsonObject;
    }
    public static JSONObject buildSuccessResponse(Object data){
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("status","1");
        jsonObject.put("info","操作成功");
        jsonObject.put("data",data);
        return jsonObject;
    }
    public static JSONObject buildSuccessResponse(){
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("status","1");
        jsonObject.put("info","操作成功");
        return jsonObject;
    }

    public static JSONObject buildFailureResponse(String info){
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("status","0");
        jsonObject.put("info",info);
        return jsonObject;
    }



}
