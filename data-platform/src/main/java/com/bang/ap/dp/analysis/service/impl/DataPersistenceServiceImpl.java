package com.bang.ap.dp.analysis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.dto.FrequenceInRoomDTO;
import com.bang.ap.dp.analysis.dto.RoomUseTimeDTO;
import com.bang.ap.dp.analysis.service.DataPesistenceService;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.web.mapper.FrequenceInRoomMapper;
import com.bang.ap.dp.web.mapper.RoomUsedTimeLengthMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DataPersistenceServiceImpl implements DataPesistenceService {

    @Autowired
    HikvisionUtil hikvisionUtil;

    @Autowired
    FrequenceInRoomMapper frequenceInRoomMapper;


    @Autowired
    private RoomUsedTimeLengthMapper roomUsedTimeLengthMapper;

    @Override
    public void saveFrequenceInRoom(Date date) {
        //1、调用海康接口获取，获取人脸抓拍事件，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"，
        String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.getYesterday()), DPConstant.DATE_FORMAT);
        String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(date), DPConstant.DATE_FORMAT);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 1000);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("eca9e1993abe4488bacb875fd68e5935");
        jsonObject.put("cameraIndexCodes", jsonArray);
        int total = 0;
        try {
            String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_NORMAL_, jsonObject);
            JSONObject resultObject = JSONObject.parseObject(result);
            if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
                JSONObject dataObject = (JSONObject) resultObject.get("data");
                total = (int) dataObject.get("total");
            }

            log.info("按条件查询人脸抓拍事件，请求url={},请求参数为{},请求结果为{}", UrlConstant.URL_FACE_EVENT_NORMAL_, jsonObject.toJSONString(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //2、保存数据
        try {
            FrequenceInRoomDTO frequenceInRoomDTO = new FrequenceInRoomDTO();
            int rootId = 0;
            frequenceInRoomDTO.setRoomId(rootId);
            frequenceInRoomDTO.setTimes(total);
            frequenceInRoomDTO.setCheckDate(DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
            frequenceInRoomDTO.setCreateTime(new Date());
            frequenceInRoomDTO.setUpdateTime(new Date());
            FrequenceInRoomDTO param = new FrequenceInRoomDTO();
            param.setRoomId(rootId);
            param.setCheckDate(DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
            List<FrequenceInRoomDTO> repeatCheck = frequenceInRoomMapper.getFrequenceInRoomDTO(param);
            if (null != repeatCheck && repeatCheck.size() > 0) {
                frequenceInRoomMapper.updateFrequenceInRoomDTO(frequenceInRoomDTO);
            } else {
                frequenceInRoomMapper.insertFrequenceInRoomDTO(frequenceInRoomDTO);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    }

    @Override
    public void saveRoomUseTimeLength(Date date) {
        //1、调用海康接口获取人脸抓拍事件，查询所有时间，获取数据的开始结束时间
        // String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.getYesterday()), DPConstant.DATE_FORMAT);
        String startTime = "2020-11-26T17:30:08.000+08:00";
        String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(date), DPConstant.DATE_FORMAT);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageSize", 1000);
        jsonObject.put("pageNo", 1);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        String firstTime = null;
        String lastTIme = null;
        long interval = 0;

        try {

            String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_NORMAL_, jsonObject);
            JSONObject resultObject = JSONObject.parseObject(result);
            if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
                JSONObject dataObject = (JSONObject) resultObject.get("data");
                JSONArray jsonArray = (JSONArray) dataObject.get("list");
                if (jsonArray != null && jsonArray.size() > 0) {

                    for (int i = 0; i < jsonArray.size(); i++) {
                        String time = (String) jsonArray.getJSONObject(i).get("eventTime");
                        if (time != null && time.contains(DPConstant.DATE_FORMAT_DATETYPE)) {
                            //限定范围
                        }
                        if (StringUtils.isNotEmpty(firstTime)) {
                            firstTime = DPTimeUtil.getEarlier(time, firstTime);
                        } else {
                            firstTime = time;
                        }

                        if (StringUtils.isNotEmpty(lastTIme)) {
                            lastTIme = DPTimeUtil.getLater(time, lastTIme);
                        } else {
                            lastTIme = time;
                        }

                    }
                }
                interval = DPTimeUtil.getInterval(firstTime, lastTIme);
            }

            log.info("按条件查询人脸抓拍事件，请求url={},请求参数为{},请求结果为{}", UrlConstant.URL_FACE_EVENT_NORMAL_, jsonObject.toJSONString(), result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        //2、保存数据
        int roomId = 0;
        RoomUseTimeDTO roomUseTimeDTO = new RoomUseTimeDTO();
        roomUseTimeDTO.setRoomId(roomId);
        roomUseTimeDTO.setTimeLength(interval);
        roomUseTimeDTO.setDate(DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
        roomUseTimeDTO.setCreateTime(new Date());
        roomUseTimeDTO.setUpdateTime(new Date());
        roomUseTimeDTO.setEarlierTime(DPTimeUtil.isoStr2utc8Str(firstTime, DPConstant.DATE_FORMAT));
        roomUseTimeDTO.setLaterTime(DPTimeUtil.isoStr2utc8Str(lastTIme, DPConstant.DATE_FORMAT));
        RoomUseTimeDTO param = new RoomUseTimeDTO();
        param.setRoomId(roomId);
        param.setDate(DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
        List<RoomUseTimeDTO> repeatDataCheck = roomUsedTimeLengthMapper.getRoomUseTimeDTO(param);
        if (null != repeatDataCheck && repeatDataCheck.size() > 0) {
            roomUsedTimeLengthMapper.updateRoomUseTimeDTO(roomUseTimeDTO);
        } else {
            roomUsedTimeLengthMapper.insertRoomUseTimeDTO(roomUseTimeDTO);
        }


    }
}
