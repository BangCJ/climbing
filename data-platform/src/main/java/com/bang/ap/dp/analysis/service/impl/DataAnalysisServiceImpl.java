package com.bang.ap.dp.analysis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.dto.*;
import com.bang.ap.dp.analysis.service.DataAnalysisService;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.utils.PictureUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.mapper.FrequenceInRoomMapper;
import com.bang.ap.dp.web.mapper.RoomUsedTimeLengthMapper;
import com.bang.ap.dp.web.mapper.StrangerInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.field.ImpreciseDateTimeField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "ap", name = "test-enable", havingValue = "false", matchIfMissing = true)
public class DataAnalysisServiceImpl implements DataAnalysisService {

    @Autowired
    private FrequenceInRoomMapper frequenceInRoomMapper;

    @Autowired
    private RoomUsedTimeLengthMapper roomUsedTimeLengthMapper;

    @Autowired
    private StrangerInfoMapper strangerInfoMapper;

    @Autowired
    HikvisionUtil hikvisionUtil;

    @Value("${picture.url}")
    private String picturl;


    @Override
    public List<FrequenceInRoomDTO> getFrequenceOfRoomInOneWeek(String id) {
        //查询最近七天内的实验室${id}，进出人次
        List<FrequenceInRoomDTO> frequenceInRoomDTOList = frequenceInRoomMapper.getTop7FrequenceByRoomId(id);
        return frequenceInRoomDTOList;
    }

    @Override
    public List<RoomUseTimeDTO> getRoomUsedTimeInOneWeek(int id) {
        RoomUseTimeDTO roomUseTimeDTO = new RoomUseTimeDTO();
        roomUseTimeDTO.setRoomId(id);
        roomUseTimeDTO.setTimeLength(0);
        //查询最近七天内的实验室${id}，使用时常
        List<RoomUseTimeDTO> roomUseTimeDTOList = roomUsedTimeLengthMapper.getTop7RoomUseTime(roomUseTimeDTO);
        return roomUseTimeDTOList;
    }

    @Override
    public int getRooUserdRateInOneWeek(int id) {
        long allTime = 0;
        RoomUseTimeDTO roomUseTimeDTO = new RoomUseTimeDTO();
        roomUseTimeDTO.setRoomId(id);
        //查询最近七天内的实验室${id}，使用时常
        List<RoomUseTimeDTO> roomUseTimeDTOList = roomUsedTimeLengthMapper.getTop7RoomUseTime(roomUseTimeDTO);
        if (roomUseTimeDTOList == null || roomUseTimeDTOList.size() == 0) {
            return 0;
        }
        for (int i = 0; i < roomUseTimeDTOList.size(); i++) {
            allTime = allTime + roomUseTimeDTOList.get(i).getTimeLength();
        }
        return (int) (allTime * 100 / (24 * roomUseTimeDTOList.size()));
    }

    @Override
    public HighFrequenceResponseDTO getHighFrequenceInfo() {
        //调用海康接口"按条件查询重点人员事件"获取数据，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
        String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.get7DaysAgo()), DPConstant.DATE_FORMAT);
        String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(new Date()), DPConstant.DATE_FORMAT);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("eca9e1993abe4488bacb875fd68e5935");
        jsonObject.put("cameraIndexCodes", jsonArray);
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 1000);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        jsonObject.put("similarity", 30);

        Set<String> nameSet = new HashSet<>();
        Map<String, String> nameAndFaceUrl = new HashMap<>();
        nameAndFaceUrl.put("徐剑坤",picturl+"standard/xujiankun.png");
        nameAndFaceUrl.put("覃奔",picturl+"standard/qinben.png");
        Map<String, Integer> nameAndTimes = new HashMap<>();
        List<ImportantPeopleDTO>importantPeopleDTOList=new ArrayList<>();
        try {
            String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_IMPORTANT_, jsonObject);
            JSONObject resultObject = JSONObject.parseObject(result);
            if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
                JSONObject dataObject = (JSONObject) resultObject.get("data");
                JSONArray jsonArrayList = (JSONArray) dataObject.get("list");
                if (jsonArrayList != null && jsonArrayList.size() > 0) {

                    for (int i = 0; i < jsonArrayList.size(); i++) {
                        JSONArray targetInfoList = (JSONArray) jsonArrayList.getJSONObject(i).get("targetInfoList");
                        JSONObject targetInfo = (JSONObject) targetInfoList.get(0);
                        String name = (String) targetInfo.get("name");
                        String faceUrl = (String) targetInfo.get("faceUrl");
                        nameSet.add(name);
                        if (null==nameAndFaceUrl.get(name)){
                            nameAndFaceUrl.put(name,faceUrl);
                        }

                        if (nameAndTimes.get(name) == null) {
                            nameAndTimes.put(name, 1);
                        } else {
                            nameAndTimes.put(name, nameAndTimes.get(name) + 1);
                        }

                        JSONObject snapObject = jsonArrayList.getJSONObject(i).getJSONObject("snapInfo");
                        JSONObject pictureParam=new JSONObject();
                        pictureParam.put("url",snapObject.getString("snapUrl"));

                        //处理重点人员列表信息
                        String pactureDown=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_DOWN_,pictureParam);
                        JSONObject pictureDownObject= JSONObject.parseObject(pactureDown);
                        String data=pictureDownObject.get("data").toString();
                        String bkgPictureName= UUID.randomUUID().toString()+".jpg";
                        String bkgUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/important/" + bkgPictureName;
                        if (PictureUtil.GenerateImage(data,bkgUrlBak)){
                            ImportantPeopleDTO importantPeopleDTO=new ImportantPeopleDTO();
                            importantPeopleDTO.setPicture(picturl+"important/"+bkgPictureName);
                            importantPeopleDTO.setCameraName(snapObject.getString("cameraName"));
                            importantPeopleDTO.setEventTime(DPTimeUtil.isoStr2utc8Str(snapObject.getString("eventTime"),DPConstant.DATE_FORMAT));
                            importantPeopleDTO.setName(name);
                            importantPeopleDTO.setPicture2(nameAndFaceUrl.get(name));
                            importantPeopleDTOList.add(importantPeopleDTO);


                        }


                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }




        //处理高频人员信息
        HighFrequenceResponseDTO responseDTO = new HighFrequenceResponseDTO();
        List<HighFrequenceDTO> highFrequenceDTOList = new ArrayList<>();
        for (String name : nameSet) {
            HighFrequenceDTO highFrequenceDTO = new HighFrequenceDTO();
            highFrequenceDTO.setId(null);
            highFrequenceDTO.setCode(null);
            highFrequenceDTO.setName(name);
            highFrequenceDTO.setTimes(nameAndTimes.get(name).toString());
            highFrequenceDTO.setPicture(nameAndFaceUrl.get(name));
            highFrequenceDTOList.add(highFrequenceDTO);
        }

        Iterator<Map.Entry<String, Integer>> entries = nameAndTimes.entrySet().iterator();
        String finalName = "";
        int standardValue = 0;
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            if (entry.getValue() > standardValue) {
                finalName = entry.getKey();
                standardValue = entry.getValue();
            }
        }
        HighestFrequenceDTO highestFrequenceDTO = new HighestFrequenceDTO();
        highestFrequenceDTO.setHighestName(finalName);
        highestFrequenceDTO.setHighestTimes(nameAndTimes.get(finalName).toString());
        highestFrequenceDTO.setPicture(nameAndFaceUrl.get(finalName));

        //拼装返回结果
        responseDTO.setAllData(highFrequenceDTOList);
        responseDTO.setHighest(highestFrequenceDTO);
        responseDTO.setImportantPeople(importantPeopleDTOList);
        return responseDTO;
    }

    @Override
    public StrangerResponseDTO getStrangerInfo() {
        List<StrangerInfoDTO> strangerInfoDTOList = new ArrayList<>();
        strangerInfoDTOList = strangerInfoMapper.getTop10StrangerInfoDTO(new StrangerInfoDTO());
        for (int i = 0; i < strangerInfoDTOList.size(); i++) {
            //处理图片地址问题，后期改为fastdfs存储
            String data = PictureUtil.GetImageStr(strangerInfoDTOList.get(i).getSnapUrlBak());
            String bkgPictureName = strangerInfoDTOList.get(i).getSnapUrlPictureNameBak();
            String bkgUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/stranger/" + bkgPictureName;
            PictureUtil.GenerateImage(data, bkgUrlBak);
            strangerInfoDTOList.get(i).setPicture(picturl +"stranger/"+ bkgPictureName);


            String backData = PictureUtil.GetImageStr(strangerInfoDTOList.get(i).getBkgUrlBak());
            String backPictureName = strangerInfoDTOList.get(i).getBkgUrlPictureNameBak();
            String backUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/stranger/" + backPictureName;
            PictureUtil.GenerateImage(backData, backUrlBak);
            strangerInfoDTOList.get(i).setBkgUrl(picturl +"stranger/"+ backPictureName);

            
            //times
            strangerInfoDTOList.get(i).setTimes(strangerInfoDTOList.get(i).getTotalSimilar() + "");
            strangerInfoDTOList.get(i).setEventTime(DPTimeUtil.isoStr2utc8Str(strangerInfoDTOList.get(i).getEventTime(),DPConstant.DATE_FORMAT));

        }
        StrangerResponseDTO strangerResponseDTO = new StrangerResponseDTO();
        strangerResponseDTO.setAllData(strangerInfoDTOList);


        //查询最近7天
        //调用海康接口"按条件查询重点人员事件"获取数据，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
        String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.get7DaysAgo()), DPConstant.DATE_FORMAT);
        String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(new Date()), DPConstant.DATE_FORMAT);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.add("eca9e1993abe4488bacb875fd68e5935");
        jsonObject.put("cameraIndexCodes", jsonArray);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        jsonObject.put("similarity", 30);
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 1000);
        String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_IMPORTANT_, jsonObject);
        JSONObject resultObject = JSONObject.parseObject(result);
        int tatal = 0;
        if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
            tatal = resultObject.getJSONObject("data").getIntValue("total");

        }

        int totalStranger = strangerInfoMapper.getAccount(DPTimeUtil.formatDate(DPTimeUtil.get7DaysAgo(), DPConstant.DATE_FORMAT_DATETYPE));
        int rate = totalStranger * 100 / (totalStranger + tatal);

        strangerResponseDTO.setRate(String.valueOf(rate));
        return strangerResponseDTO;
    }

    @Override
    public String getBispectrumUrl() {
        return "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4107912782,1042503672&fm=26&gp=0.jpg";
    }

    @Override
    public WarningResponseDTO getWarningInfo() {
        WarningResponseDTO responseDTO = new WarningResponseDTO();
        WarningAmountDTO amountDTO = new WarningAmountDTO();
        List<WarningTypeDTO> warningTypeDTOList = new ArrayList<>();
        List<WarningDetailDTO> warningDetailDTOList = new ArrayList<>();
        List<WarningDailyAmountDTO> dailyAmountDTOS = new ArrayList<>();

        WarningTypeDTO warningTypeDTO1 = new WarningTypeDTO("yuejie", "越界预警", "10");
        WarningTypeDTO warningTypeDTO2 = new WarningTypeDTO("chuanganqi", "传感器预警", "70");
        WarningTypeDTO warningTypeDTO3 = new WarningTypeDTO("qushi", "趋势预警", "10");
        WarningTypeDTO warningTypeDTO4 = new WarningTypeDTO("other", "其他预警", "10");
        warningTypeDTOList.add(warningTypeDTO1);
        warningTypeDTOList.add(warningTypeDTO2);
        warningTypeDTOList.add(warningTypeDTO3);
        warningTypeDTOList.add(warningTypeDTO4);

        for (int i = 0; i < 7; i++) {
            WarningDetailDTO detailDTO = new WarningDetailDTO();
            detailDTO.setArea("实验室301");
            detailDTO.setTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
            detailDTO.setWarningInfo("实验室温度当前值为4" + i + "度，超过阈值");
            detailDTO.setWarningType("传感器预警");
            warningDetailDTOList.add(detailDTO);
        }

        Date today = new Date();
        List<WarningDailyAmountDTO> dailyAmountDTOList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            WarningDailyAmountDTO dailyAmountDTO = new WarningDailyAmountDTO();
            dailyAmountDTO.setAmount(String.valueOf(i));
            dailyAmountDTO.setDate(DPTimeUtil.formatDate(today, DPConstant.DATE_FORMAT_DATETYPE));
            dailyAmountDTOList.add(dailyAmountDTO);
            today = DPTimeUtil.getYesterday();
        }
        amountDTO.setTodayAmount("0");
        amountDTO.setHistoryAmount("111");
        amountDTO.setDailyAmount(dailyAmountDTOList);

        responseDTO.setWarningType(warningTypeDTOList);
        responseDTO.setWarningAmount(amountDTO);
        responseDTO.setWarningDetail(warningDetailDTOList);


        return responseDTO;
    }

    @Override
    public List<DataChangeDTO> getDataChangeForFire() {
        List<DataChangeDTO> response = new ArrayList<>();
        DataChangeDTO dataChangeDTO1 = new DataChangeDTO(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT), "1", "1", "1");

        response.add(dataChangeDTO1);

        return response;
    }

    @Override
    public List<DataRealTimeDTO> getRealTimeDataForFire() {
        List<DataRealTimeDTO> response = new ArrayList<>();
        DataRealTimeDTO dataRealTimeDTO1 = new DataRealTimeDTO(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT), "11", "12", "11", "11", "12", "11");

        response.add(dataRealTimeDTO1);

        return response;
    }

    @Override
    public TerminalResponseDTO getTerminalInfo() {
        TerminalResponseDTO responseDTO = new TerminalResponseDTO();
        List<TerminalAmountDTO> terminalAmountDTOList = new ArrayList<>();
        TerminalHealthCheckDTO healthCheckDTO = new TerminalHealthCheckDTO();
        TerminalAmountDTO terminalAmountDTO1 = new TerminalAmountDTO("10", "摄像头", "camera");
        TerminalAmountDTO terminalAmountDTO2 = new TerminalAmountDTO("20", "传感器", "sensor");
        terminalAmountDTOList.add(terminalAmountDTO1);
        terminalAmountDTOList.add(terminalAmountDTO2);


        List<TerminalHealthCheckDTO> terminalHealthCheckDTOList = new ArrayList<>();
        TerminalHealthCheckDTO terminalHealthCheckDTO1 = new TerminalHealthCheckDTO("健康摄像头", 10);
        TerminalHealthCheckDTO terminalHealthCheckDTO2 = new TerminalHealthCheckDTO("非健康摄像头", 30);
        TerminalHealthCheckDTO terminalHealthCheckDTO3 = new TerminalHealthCheckDTO("健康传感器", 20);
        TerminalHealthCheckDTO terminalHealthCheckDTO4 = new TerminalHealthCheckDTO("非健康传感器", 40);
        terminalHealthCheckDTOList.add(terminalHealthCheckDTO1);
        terminalHealthCheckDTOList.add(terminalHealthCheckDTO2);
        terminalHealthCheckDTOList.add(terminalHealthCheckDTO3);
        terminalHealthCheckDTOList.add(terminalHealthCheckDTO4);


        responseDTO.setHealthCheck(terminalHealthCheckDTOList);
        responseDTO.setTeminal(terminalAmountDTOList);
        return responseDTO;
    }

    @Override
    public List<MonitorData> getMonitorData() {
        List<MonitorData> monitorDataList = new ArrayList<>();
        MonitorData monitorData = new MonitorData();
        MonitorData monitorData2 = new MonitorData();
        monitorData.setId(1);
        monitorData.setCode("CO");
        monitorData.setName("一氧化碳");
        monitorData.setUnit("");
        monitorData.setValue("0.4");
        monitorData.setMonitorType("");
        monitorData.setOriginDateTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));

        monitorData2.setId(2);
        monitorData2.setCode("temp");
        monitorData2.setName("温度");
        monitorData2.setUnit("摄氏度");
        monitorData2.setValue("11");
        monitorData2.setMonitorType("");
        monitorData2.setOriginDateTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));

        monitorDataList.add(monitorData);
        monitorDataList.add(monitorData2);
        return monitorDataList;
    }

    @Override
    public MonitorData getMonitorDataByType(String type) {
        MonitorData monitorData = new MonitorData();
        monitorData.setValue("0.4");
        monitorData.setId(1);
        monitorData.setCode("CO");
        monitorData.setName("一氧化碳");
        monitorData.setUnit("");
        monitorData.setMonitorType("");
        monitorData.setOriginDateTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
        return monitorData;
    }
}
