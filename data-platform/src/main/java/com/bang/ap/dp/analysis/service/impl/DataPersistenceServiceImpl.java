package com.bang.ap.dp.analysis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.dto.*;
import com.bang.ap.dp.analysis.service.DataPesistenceService;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.utils.PictureUtil;
import com.bang.ap.dp.web.mapper.FrequenceInRoomMapper;
import com.bang.ap.dp.web.mapper.ImportantPeopleMapper;
import com.bang.ap.dp.web.mapper.RoomUsedTimeLengthMapper;
import com.bang.ap.dp.web.mapper.StrangerInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DataPersistenceServiceImpl implements DataPesistenceService {

    @Value("${ap.time.length:-1}")
    private int timeLength;

    @Autowired
    HikvisionUtil hikvisionUtil;

    @Autowired
    FrequenceInRoomMapper frequenceInRoomMapper;

    @Autowired
    private RoomUsedTimeLengthMapper roomUsedTimeLengthMapper;

    @Autowired
    private StrangerInfoMapper strangerInfoMapper;

    @Autowired
    private ImportantPeopleMapper importantPeopleMapper;


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
                log.info("saveFrequenceInRoom,当前存在当天{}数据，做更新操作", DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
            } else {
                frequenceInRoomMapper.insertFrequenceInRoomDTO(frequenceInRoomDTO);
                log.info("saveFrequenceInRoom,当前不存在当天{}数据，做新增操作", DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));
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
            log.info("saveRoomUseTimeLength,当前存在当天{}数据，做更新操作", DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));

        } else {
            roomUsedTimeLengthMapper.insertRoomUseTimeDTO(roomUseTimeDTO);
            log.info("saveRoomUseTimeLength,当前不存在当天{}数据，做新增操作", DPTimeUtil.getYesterday(DPConstant.DATE_FORMAT_DATETYPE));

        }

    }

    @Override
    public synchronized void saveStrangerInfo(Date date) {
        log.info("schedule3: start to saveStrangerInfo ! timeLenth={}", timeLength);
        List<StrangerInfoDTO> strangerInfoDTOList = new ArrayList<>();
        try {
            //调用海康接口"按条件查询陌生人事件"获取数据，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
            String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.getNDaysAgo(timeLength)), DPConstant.DATE_FORMAT);
            String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(date), DPConstant.DATE_FORMAT);
            String result = this.getStrangerFromHik(startTime, endTime, "eca9e1993abe4488bacb875fd68e5935");
            JSONObject resultObject = JSONObject.parseObject(result);
            if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
                JSONArray jsonArrayList = resultObject.getJSONObject("data").getJSONArray("list");
                if (jsonArrayList != null && jsonArrayList.size() > 0) {
                    for (int i = 0; i < jsonArrayList.size(); i++) {
                        StrangerInfoDTO strangerInfoDTO = jsonArrayList.getObject(i, StrangerInfoDTO.class);

                        /*去重，准备持久化*/
                        //根据 bkg_url+snap_url查询陌生人信息,如果存在则跳过
                        List<StrangerInfoDTO> checkStrangerDTOS = strangerInfoMapper.getStrangerInfoDTO(strangerInfoDTO);
                        if (null == checkStrangerDTOS || checkStrangerDTOS.size() == 0) {
                            strangerInfoDTO.setDataTime(DPTimeUtil.isoStr2utc8Str(strangerInfoDTO.getEventTime(), DPConstant.DATE_FORMAT_DATETYPE));
                            strangerInfoDTO.setCreateTime(new Date());
                            strangerInfoDTO.setUpdateTime(new Date());
                            strangerInfoDTOList.add(strangerInfoDTO);
                        } else {
                            log.warn("saveStrangerInfo 存在重复数据{}", JSON.toJSONString(strangerInfoDTO));
                            continue;
                        }

                        /*图片另存为，在服务器固定地址和tomcat下都生成所需图片备份*/
                        String targetUrlInTomcat = this.getClass().getClassLoader().getResource("static").getFile() + "picture/stranger/";
                        PictureGeneratorInfo bkgInfo = this.doPictureGenerate(strangerInfoDTO.getBkgUrl(), targetUrlInTomcat, "Stranger");
                        PictureGeneratorInfo snapInfo = this.doPictureGenerate(strangerInfoDTO.getSnapUrl(), targetUrlInTomcat, "Stranger");
                        strangerInfoDTO.setBkgUrlBak(bkgInfo.getUrl());
                        strangerInfoDTO.setBkgUrlPictureNameBak(bkgInfo.getName());
                        strangerInfoDTO.setSnapUrlBak(snapInfo.getUrl());
                        strangerInfoDTO.setSnapUrlPictureNameBak(snapInfo.getName());
                    }
                }
                //写库
                if (strangerInfoDTOList.size() > 0) {
                    strangerInfoMapper.insertStrangerInfoDTOList(strangerInfoDTOList);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("schedule3: end to saveStrangerInfo !");


    }

    /**
     * 保存重点人员信息
     *
     * @param date
     */
    @Override
    public synchronized void saveImportantPeopleInfo(Date date) {
        log.info("schedule4: start to saveImportantPeopleInfo !timeLenth={}", timeLength);
        try {
            //调用海康接口"按条件查询重点人员"获取数据，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
            String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.getNDaysAgo(timeLength)), DPConstant.DATE_FORMAT);
            String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(date), DPConstant.DATE_FORMAT);
            String result = this.getImportantPeople(startTime, endTime, 15, "eca9e1993abe4488bacb875fd68e5935");
            JSONObject resultObject = JSONObject.parseObject(result);
            if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
                JSONArray jsonArrayList = resultObject.getJSONObject("data").getJSONArray("list");
                if (jsonArrayList != null && jsonArrayList.size() > 0) {
                    List<ImportantPeopleDTO> importantPeopleDTOList = new ArrayList<>();
                    for (int i = 0; i < jsonArrayList.size(); i++) {
                        JSONObject targetObject = jsonArrayList.getJSONObject(i).getJSONArray("targetInfoList").getJSONObject(0);
                        ImportantPeopleDTO importantPeopleDTO = jsonArrayList.getJSONObject(i).getObject("snapInfo", ImportantPeopleDTO.class);
                        //去重，准备持久化
                        List<ImportantPeopleDTO> checkImportantPeopleDTOS = importantPeopleMapper.getImportantPeopleDTO(importantPeopleDTO);
                        if (null == checkImportantPeopleDTOS || checkImportantPeopleDTOS.size() == 0) {
                            importantPeopleDTO.setCreateTime(new Date());
                            importantPeopleDTO.setUpdateTime(new Date());
                            importantPeopleDTO.setDataTime(DPTimeUtil.isoStr2utc8Str(importantPeopleDTO.getEventTime(), DPConstant.DATE_FORMAT_DATETYPE));
                            importantPeopleDTOList.add(importantPeopleDTO);
                        } else {
                            log.warn("saveImportantPeopleInfo 存在重复数据{}", JSON.toJSONString(importantPeopleDTO));
                            continue;
                        }
                        //处理snapInfo.图片信息
                        String targetUrlInTomcat = this.getClass().getClassLoader().getResource("static").getFile() + "picture/important/";
                        PictureGeneratorInfo bkgInfo = this.doPictureGenerate(importantPeopleDTO.getBkgUrl(), targetUrlInTomcat, "IMP");
                        PictureGeneratorInfo snapInfo = this.doPictureGenerate(importantPeopleDTO.getSnapUrl(), targetUrlInTomcat, "IMP");
                        importantPeopleDTO.setBkgUrlBak(bkgInfo.getUrl());
                        importantPeopleDTO.setBkgUrlPictureNameBak(bkgInfo.getName());
                        importantPeopleDTO.setSnapUrlBak(snapInfo.getUrl());
                        importantPeopleDTO.setSnapUrlPictureNameBak(snapInfo.getName());
                        importantPeopleDTO.setPicture(snapInfo.getUrlInTomcat());

                        //处理targetInfo
                        importantPeopleDTO.setName(targetObject.getString("name"));
                    }
                    //数据持久化
                    if (importantPeopleDTOList.size() > 0) {
                        importantPeopleMapper.insertImportantPeopleDTOList(importantPeopleDTOList);
                    }

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("schedule4: end to saveImportantPeopleInfo !");


    }

    /**
     * 图片信息另存为
     *
     * @param pictureUrlInHik
     * @param type
     * @return
     */
    private PictureGeneratorInfo doPictureGenerate(String pictureUrlInHik, String targerUrlInTomcat, String type) {
        JSONObject pictureParam = new JSONObject();
        pictureParam.put("url", pictureUrlInHik);
        String pactureDown = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_DOWN_, pictureParam);
        JSONObject pictureDownObject = JSONObject.parseObject(pactureDown);
        String data = pictureDownObject.get("data").toString();
        String bkgPictureName = PictureUtil.getPictureName(type);
        String bkgUrlBak = "/data/data-platform/picture/" + bkgPictureName;
        //在服务器上生成备份图片
        PictureUtil.GenerateImage(data, bkgUrlBak);
        if (StringUtils.isNotEmpty(targerUrlInTomcat)) {
            //在tomcat下生成备份文件
            PictureUtil.GenerateImage(data, targerUrlInTomcat + bkgPictureName);
        }
        PictureGeneratorInfo pictureGeneratorInfo = new PictureGeneratorInfo(bkgPictureName, bkgUrlBak, targerUrlInTomcat + bkgPictureName);
        return pictureGeneratorInfo;
    }

    /**
     * 调用海康接口"按条件查询重点人员事件"获取数据，
     * 指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
     * 数据查询最近7天
     *
     * @return
     */
    private String getImportantPeople(String startTime, String endTime, int similarity, String cameraIndexCode) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.add(cameraIndexCode);
        jsonObject.put("cameraIndexCodes", jsonArray);
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 1000);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        jsonObject.put("similarity", similarity);
        String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_IMPORTANT_, jsonObject);
        return result;
    }

    /**
     * 调用海康借口查询陌生人信息
     * 指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
     *
     * @param startTime
     * @param endTime
     * @param cameraIndexCode
     * @return
     */
    private String getStrangerFromHik(String startTime, String endTime, String cameraIndexCode) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.add(cameraIndexCode);
        jsonObject.put("cameraIndexCodes", jsonArray);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 1000);
        String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_STRANGE_, jsonObject);
        return result;
    }



    @Async(value="dpThreadPool")
    @Override
    public void asyncSaveStranger(Date date){
        this.saveStrangerInfo(date);
    }

    @Async(value="dpThreadPool")
    @Override
    public void asyncSaveImportantPeople(Date date){
        this.saveImportantPeopleInfo(date);
    }
}
