package com.bang.ap.dp.receive.hikvision.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.receive.hikvision.service.HikSensorService;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.TerminalInfo;
import com.bang.ap.dp.web.mapper.MonitorDataMapper;
import com.bang.ap.dp.web.mapper.TerminalMapper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class HikSensorServiceImpl implements HikSensorService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    HikvisionUtil hikvisionUtil;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private MonitorDataMapper monitorDataMapper;

    @Override
    public boolean saveSensorInfo() {
        try {
            //查询传感器类型
            List<TerminalInfo> terminalInfoList= terminalMapper.getTerminalListByTypeAndGroup("sensor","hik");
            if (terminalInfoList!=null && terminalInfoList.size()>0){
                for (int i = 0; i <terminalInfoList.size() ; i++) {
                    //请求海康接口获取数据
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("pageNo",1);
                    jsonObject.put("pageSize",10);
                    jsonObject.put("parentIndexCode",terminalInfoList.get(i).getCode());
                    String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_SENSOR_VALUE, jsonObject);
                    JSONObject resultObject = JSONObject.parseObject(result);
                    if (StringUtils.isNotEmpty(resultObject.getString("msg"))&&resultObject.getString("msg").contains("success")){
                        JSONObject dataObject =resultObject.getJSONObject("data").getJSONArray("list").getJSONObject(0);
                        //拼装数据，并写库
                        MonitorData monitorData=new MonitorData();
                        monitorData.setMonitorType(dataObject.getString("sensorType"));
                        monitorData.setValue(dataObject.getString("latestValue"));
                        monitorData.setOriginDataTime(dataObject.getString("latestTime"));
                        monitorData.setUnit(dataObject.getString("unit"));
                        monitorData.setName(dataObject.getString("name"));
                        monitorData.setCode(dataObject.getString("indexCode"));
                        monitorData.setCreateTime(new Date());
                        monitorData.setUpdateTime(new Date());
                        monitorDataMapper.addMonitorData(monitorData);
                    }else{
                        throw new Exception("获取海康传感器监控数据失败");
                    }
                }

            }
        } catch (Exception e) {
           log.error("保存海康传感器数据失败",e);
            return false;
        }

        return true;
    }
}
