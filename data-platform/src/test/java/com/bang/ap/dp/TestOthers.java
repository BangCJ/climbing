package com.bang.ap.dp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.web.entity.MonitorData;

public class TestOthers {
    public static void main(String[] args) {
        buildResponse();
    }

    public static  void buildResponse(){
        JSONObject jsonObject =new JSONObject();
        JSONArray dataJSONArray=new JSONArray();

        jsonObject.put("status","1");
        jsonObject.put("info","查询数据成功");



        JSONArray finalJSONarray =new JSONArray();
        for (int i = 1; i <7 ; i++) {
            MonitorData monitorData=new MonitorData();
            monitorData.setId(i);
            monitorData.setCode("type"+i);
            monitorData.setName("监测数据类型"+i);
            monitorData.setMonitorType("所属大分类，实时数据应该用不着");
            monitorData.setUnit("单位");
            monitorData.setValue("数值"+i);
            monitorData.setOriginDateTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
            finalJSONarray.add(monitorData);

        }

        jsonObject.put("data",finalJSONarray);

        System.out.println(jsonObject.toJSONString());

    }
    public static  void buildResponse2(){
        JSONObject jsonObject =new JSONObject();
        JSONObject jsonObject2 =new JSONObject();
        jsonObject.put("status","1");
        jsonObject.put("info","查询数据成功");
        jsonObject2.put("picture","http://图片链接");
        jsonObject2.put("name","张三");
        jsonObject2.put("times","25");
        jsonObject.put("data",jsonObject2);

        System.out.println(jsonObject.toJSONString());

    }
}
