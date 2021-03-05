package com.bang.ap.dp.receive.hikvision.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.receive.hikvision.service.HikSensorService;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.TerminalInfo;
import com.bang.ap.dp.web.mapper.MonitorDataMapper;
import com.bang.ap.dp.web.mapper.TerminalMapper;
import com.bang.ap.dp.web.service.WarningService;
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

    @Autowired
    private WarningService warningService;

    @Override
    public boolean saveSensorInfo() {
        try {
            //查询传感器类型
            List<TerminalInfo> terminalInfoList = terminalMapper.getTerminalListByTypeAndGroup("sensor", "hik");
            if (terminalInfoList != null && terminalInfoList.size() > 0) {
                for (int i = 0; i < terminalInfoList.size(); i++) {
                    //请求海康接口获取数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pageNo", 1);
                    jsonObject.put("pageSize", 10);
                    jsonObject.put("parentIndexCode", terminalInfoList.get(i).getCode());
                    String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_SENSOR_VALUE, jsonObject);
                    JSONObject resultObject = JSONObject.parseObject(result);
                    if (StringUtils.isNotEmpty(resultObject.getString("msg")) && resultObject.getString("msg").contains("success")) {
                        JSONObject dataObject = resultObject.getJSONObject("data").getJSONArray("list").getJSONObject(0);
                        //拼装数据，并写库
                        MonitorData monitorData = new MonitorData();
                        monitorData.setMonitorType(dataObject.getString("sensorType"));
                        monitorData.setValue(dataObject.getString("latestValue"));
                        monitorData.setOriginDataTime(dataObject.getString("latestTime"));
                        monitorData.setUnit(dataObject.getString("unit"));
                        monitorData.setName(dataObject.getString("name"));
                        monitorData.setCode(dataObject.getString("indexCode"));
                        monitorData.setCreateTime(new Date());
                        monitorData.setUpdateTime(new Date());

                        List<MonitorData> existedMonitorList = monitorDataMapper.getMonitorDataByMonitorTypeAndOriginDataTime(dataObject.getString("sensorType"), dataObject.getString("latestTime"));
                        if (existedMonitorList != null && existedMonitorList.size() > 0) {
                            log.debug("saveSensorInfo 时存在相同数据。type={},time={}", dataObject.getString("sensorType"), dataObject.getString("latestTime"));
                            continue;
                        }

                        monitorDataMapper.addMonitorData(monitorData);

                        try {
                            warningService.checkDateForWarning(monitorData);
                        } catch (Exception e) {
                            log.error("checkDateForWarning error", e);
                        }
                    } else {
                        throw new Exception("获取海康传感器监控数据失败");
                    }
                }

            }
        } catch (Exception e) {

            log.error("保存海康传感器数据失败", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean saveFireSensor() {
        //请求海康接口获取数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 10);
        jsonObject.put("includeDown", 1);
        jsonObject.put("regionIndexCode", "4a069cee-8331-4b6c-8e36-4ac403fa812c");
        String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FIRESENSOR_VALUE, jsonObject);
        JSONObject resultObject = JSONObject.parseObject(result);
        if (StringUtils.isNotEmpty(resultObject.getString("msg")) && resultObject.getString("msg").contains("success")) {
            JSONObject valueObject = resultObject.getJSONObject("data").getJSONArray("rows").getJSONObject(0);
            JSONArray valueArray = valueObject.getJSONArray("values");
            if (valueArray != null && valueArray.size() > 0) {
                for (int i = 0; i < valueArray.size(); i++) {
                    //拼装数据，并写库
                    MonitorData monitorData = new MonitorData();
                    String typeCode = valueArray.getJSONObject(i).getString("monitorTypeCode");
                    if ("254024".equals(typeCode)) {
                        //电气温度
                        monitorData.setMonitorType("electricalTemperature");
                        String value = valueArray.getJSONObject(i).getString("monitorValue").replace("℃", "");
                        monitorData.setValue(value);
                        monitorData.setUnit("℃");
                    } else if ("254004".equals(typeCode)) {
                        //剩余电流
                        monitorData.setMonitorType("residualCurrent");
                        String value = valueArray.getJSONObject(i).getString("monitorValue").replace("mA", "");
                        monitorData.setValue(value);
                        monitorData.setUnit("mA");
                    }
                    monitorData.setOriginDataTime(DPTimeUtil.isoStr2utc8Str(valueObject.getString("updateTime"), DPConstant.DATE_FORMAT));
                    monitorData.setName(valueArray.getJSONObject(i).getString("monitorTypeName"));
                    monitorData.setCode(valueObject.getString("indexCode"));

                    monitorData.setCreateTime(new Date());
                    monitorData.setUpdateTime(new Date());

                    List<MonitorData> existedMonitorList = monitorDataMapper.getMonitorDataByMonitorTypeAndOriginDataTime(monitorData.getMonitorType(), monitorData.getOriginDataTime());
                    if (existedMonitorList != null && existedMonitorList.size() > 0) {
                        log.debug("saveSensorInfo 时存在相同数据。type={},time={}", monitorData.getMonitorType(), monitorData.getOriginDataTime());
                        continue;
                    }

                    try {
                        monitorDataMapper.addMonitorData(monitorData);
                        warningService.checkDateForWarning(monitorData);
                    } catch (Exception e) {
                        log.error("保存电器火灾相关传感器数据时出错", e);
                    }
                }
            }
        }
        return true;
    }
}
