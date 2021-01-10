package com.bang.ap.dp.analysis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.dto.FrequenceInRoomDTO;
import com.bang.ap.dp.analysis.dto.RoomUseTimeDTO;
import com.bang.ap.dp.analysis.dto.StrangerInfoDTO;
import com.bang.ap.dp.analysis.service.DataPesistenceService;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.utils.PictureUtil;
import com.bang.ap.dp.web.mapper.FrequenceInRoomMapper;
import com.bang.ap.dp.web.mapper.RoomUsedTimeLengthMapper;
import com.bang.ap.dp.web.mapper.StrangerInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DataPersistenceServiceImpl implements DataPesistenceService {

    @Autowired
    HikvisionUtil hikvisionUtil;

    @Autowired
    FrequenceInRoomMapper frequenceInRoomMapper;

    @Autowired
    private RoomUsedTimeLengthMapper roomUsedTimeLengthMapper;

    @Autowired
    private StrangerInfoMapper strangerInfoMapper;


    @Override
    public void saveFrequenceInRoom(Date date) {
        log.info("schedule1: start to saveFrequenceInRoom !");
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
            } else {
                log.error("saveFrequenceInRoom中，按条件查询人脸抓拍事件");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //2、保存数据
        try {
            FrequenceInRoomDTO frequenceInRoomDTO = new FrequenceInRoomDTO();
            int rootId = 1;
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
                log.info("saveFrequenceInRoom,当前存在当天{}数据，做更新操作",DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
            } else {
                frequenceInRoomMapper.insertFrequenceInRoomDTO(frequenceInRoomDTO);
                log.info("saveFrequenceInRoom,当前不存在当天{}数据，做新增操作",DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    }

    @Override
    public void saveRoomUseTimeLength(Date date) {
        log.info("schedule2: start to saveRoomUseTimeLength !");
        //1、调用海康接口获取人脸抓拍事件，查询所有时间，获取数据的开始结束时间
        String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.getYesterday()), DPConstant.DATE_FORMAT);
        // String startTime = "2020-11-26T17:30:08.000+08:00";
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
                    interval = DPTimeUtil.getInterval(firstTime, lastTIme);
                }

            } else {
                log.error("saveRoomUseTimeLength,按条件查询人脸抓拍事件查询失败");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        //2、保存数据
        int roomId = 1;
        RoomUseTimeDTO roomUseTimeDTO = new RoomUseTimeDTO();
        roomUseTimeDTO.setRoomId(roomId);
        roomUseTimeDTO.setTimeLength(interval);
        roomUseTimeDTO.setDate(DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
        roomUseTimeDTO.setCreateTime(new Date());
        roomUseTimeDTO.setUpdateTime(new Date());
        if (firstTime != null) {
            roomUseTimeDTO.setEarlierTime(DPTimeUtil.isoStr2utc8Str(firstTime, DPConstant.DATE_FORMAT));
        } else {
            roomUseTimeDTO.setEarlierTime(DPTimeUtil.formatDate(DPTimeUtil.getBeginTimeOfDay(DPTimeUtil.getYesterday())));
        }
        if (lastTIme != null) {
            roomUseTimeDTO.setLaterTime(DPTimeUtil.isoStr2utc8Str(lastTIme, DPConstant.DATE_FORMAT));
        } else {
            roomUseTimeDTO.setLaterTime(DPTimeUtil.formatDate(DPTimeUtil.getBeginTimeOfDay(DPTimeUtil.getYesterday())));
        }
        RoomUseTimeDTO param = new RoomUseTimeDTO();
        param.setRoomId(roomId);
        param.setDate(DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
        List<RoomUseTimeDTO> repeatDataCheck = roomUsedTimeLengthMapper.getRoomUseTimeDTO(param);
        if (null != repeatDataCheck && repeatDataCheck.size() > 0) {
            roomUsedTimeLengthMapper.updateRoomUseTimeDTO(roomUseTimeDTO);
            log.info("saveRoomUseTimeLength,当前存在当天{}数据，做更新操作",DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));

        } else {
            roomUsedTimeLengthMapper.insertRoomUseTimeDTO(roomUseTimeDTO);
            log.info("saveRoomUseTimeLength,当前不存在当天{}数据，做新增操作",DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));

        }


    }

    @Override
    public void saveStrangerInfo(Date date) {
        log.info("schedule3: start to saveStrangerInfo !");

        //调用海康接口"按条件查询陌生人事件"获取数据，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
        String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.getNDaysAgo(-1)), DPConstant.DATE_FORMAT);
        String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(new Date()), DPConstant.DATE_FORMAT);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.add("eca9e1993abe4488bacb875fd68e5935");
        jsonObject.put("cameraIndexCodes", jsonArray);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 1000);
        List<StrangerInfoDTO> strangerInfoDTOList = new ArrayList<>();

        try {
            String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_STRANGE_, jsonObject);
            JSONObject resultObject = JSONObject.parseObject(result);
            if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
                JSONObject dataObject = (JSONObject) resultObject.get("data");
                JSONArray jsonArrayList = (JSONArray) dataObject.get("list");
                if (jsonArrayList != null && jsonArrayList.size() > 0) {


                    for (int i = 0; i < jsonArrayList.size(); i++) {
                        StrangerInfoDTO strangerInfoDTO = new StrangerInfoDTO();
                        strangerInfoDTO = jsonArrayList.getObject(i, StrangerInfoDTO.class);
                        JSONObject pictureParam = new JSONObject();
                        pictureParam.put("url", strangerInfoDTO.getBkgUrl());
                        //处理图片地址
                        String pactureDown = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_DOWN_, pictureParam);
                        JSONObject pictureDownObject = JSONObject.parseObject(pactureDown);
                        String data = pictureDownObject.get("data").toString();
                        String bkgPictureName = UUID.randomUUID().toString() + ".jpg";
                        String bkgUrlBak = "/data/data-platform/picture/" + bkgPictureName;
                        PictureUtil.GenerateImage(data, bkgUrlBak);
                        strangerInfoDTO.setBkgUrlBak(bkgUrlBak);
                        strangerInfoDTO.setBkgUrlPictureNameBak(bkgPictureName);
                        //strangerInfoDTO.setBkgData(data);


                        JSONObject snapPictureParam = new JSONObject();
                        snapPictureParam.put("url", strangerInfoDTO.getSnapUrl());
                        //处理snap图片地址
                        String snapPictureDown = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_DOWN_, snapPictureParam);
                        String snapData = JSONObject.parseObject(snapPictureDown).get("data").toString();
                        String snapPictureName = UUID.randomUUID().toString() + ".jpg";
                        String snapUrlBak = "/data/data-platform/picture/" + bkgPictureName;
                        PictureUtil.GenerateImage(snapData, snapUrlBak);
                        strangerInfoDTO.setSnapUrlBak(snapUrlBak);
                        strangerInfoDTO.setSnapUrlPictureNameBak(snapPictureName);

                        //strangerInfoDTO.setSnapData(snapData);


                        //处理以图搜图
                        JSONObject facePicBinaryJSONObject = new JSONObject();
                        facePicBinaryJSONObject.put("facePicBinaryData", snapData);
                        facePicBinaryJSONObject.put("pageNo", 1);
                        facePicBinaryJSONObject.put("pageSize", 20);
                        facePicBinaryJSONObject.put("searchNum", 50);
                        facePicBinaryJSONObject.put("startTime", startTime);
                        facePicBinaryJSONObject.put("endTime", endTime);
                        facePicBinaryJSONObject.put("minSimilarity", 50);
                        facePicBinaryJSONObject.put("maxSimilarity", 100);

                        String facePicBinaryResult = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_CAPTURESEARCH, facePicBinaryJSONObject);
                        JSONObject facePicBinaryResultObject = JSONObject.parseObject(facePicBinaryResult);
                        if (null != facePicBinaryResultObject.get("msg") && "success".equals(facePicBinaryResultObject.get("msg"))) {
                            int total = facePicBinaryResultObject.getJSONObject("data").getInteger("total");
                            strangerInfoDTO.setTotalSimilar(total);
                        }
                        strangerInfoDTO.setDataTime(DPTimeUtil.formatDate(date, DPConstant.DATE_FORMAT_DATETYPE));

                        strangerInfoDTO.setCreateTime(new Date());
                        strangerInfoDTO.setUpdateTime(new Date());

                        strangerInfoDTOList.add(strangerInfoDTO);

                    }
                }
                //写库
                if (strangerInfoDTOList.size() > 0) {
                    StrangerInfoDTO strangerInfoParam = new StrangerInfoDTO();
                    strangerInfoParam.setDataTime(DPTimeUtil.formatDate(DPTimeUtil.getYesterday(), DPConstant.DATE_FORMAT_DATETYPE));
                    List<StrangerInfoDTO> checkList = strangerInfoMapper.getStrangerInfoDTO(strangerInfoParam);
                    if (checkList != null && checkList.size() > 0) {
                        log.info("saveStrangerInfo:持久化时，数据库存在当天{}数据,不做其他操作",DPTimeUtil.getYesterday().toString());
                        return;
                    } else {
                        strangerInfoMapper.insertStrangerInfoDTOList(strangerInfoDTOList);
                        log.info("saveStrangerInfo:持久化时，数据库不存在当天{}数据,做新增操作",DPTimeUtil.getYesterday().toString());
                    }
                }else{
                    log.info("saveStrangerInfo:持久化时，从Hil获取当天{}数据为空,不做其他操作",DPTimeUtil.getYesterday().toString());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    }
}
