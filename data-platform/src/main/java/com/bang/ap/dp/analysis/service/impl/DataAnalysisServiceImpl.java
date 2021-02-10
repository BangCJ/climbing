package com.bang.ap.dp.analysis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.dto.*;
import com.bang.ap.dp.analysis.service.DataAnalysisService;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.cons.WarningTypeConst;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.utils.PictureUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.WarningInfo;
import com.bang.ap.dp.web.entity.WarningTypeInfo;
import com.bang.ap.dp.web.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
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
    private ImportantPeopleMapper importantPeopleMapper;

    @Autowired
    private MonitorDataMapper monitorDataMapper;

    @Autowired
    private WarningMapper warningMapper;

    @Autowired
    private WarningTypeMapper warningTypeMapper;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    HikvisionUtil hikvisionUtil;

    @Value("${picture.url}")
    private String picturl;

    @Value("${relation.name.picture}")
    private String namePictureRelation;


    /**
     * 查询近七日的实验室${id}的进出人次
     *
     * @param id
     * @return
     */
    @Override
    public List<FrequenceInRoomDTO> getFrequenceOfRoomInOneWeek(String id) {
        //查询最近七天内的实验室${id}，进出人次
        List<FrequenceInRoomDTO> frequenceInRoomDTOList = frequenceInRoomMapper.getTop7FrequenceByRoomId(id);
        return frequenceInRoomDTOList;
    }

    /**
     * 查询最近七天内的实验室${id}，使用时长
     *
     * @param id 实验室id
     * @return
     */
    @Override
    public List<RoomUseTimeDTO> getRoomUsedTimeInOneWeek(int id) {
        RoomUseTimeDTO roomUseTimeDTO = new RoomUseTimeDTO();
        roomUseTimeDTO.setRoomId(id);
        roomUseTimeDTO.setTimeLength(0);
        //查询最近七天内的实验室${id}，使用时长
        List<RoomUseTimeDTO> roomUseTimeDTOList = roomUsedTimeLengthMapper.getTop7RoomUseTime(roomUseTimeDTO);
        return roomUseTimeDTOList;
    }

    /**
     * 查询近七天，实验室的使用时长占比
     * ${有人的时长}/24
     *
     * @param id 实验室id
     * @return
     */
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

    /**
     * 获取高频使用人员信息
     *
     * @return
     */
    @Override
    public HighFrequenceResponseDTO getHighFrequenceInfo() {
        List<ImportantPeopleDTO> importantPeopleDTOList = new ArrayList<>();
        Map<String, Integer> nameAndTimesMap = new HashMap<>();
        Set<String> nameSet = new HashSet<>();
        Map<String, String> nameAndFaceUrl = this.getStandardNameAndPictureRelation();
        /*1、处理七日内的重点人员出入信息*/
        try {
            //获取七日内的重点人员进出信息
            String sevenDaysAgo = DPTimeUtil.formatDate(DPTimeUtil.getNDaysAgo(-7), DPConstant.DATE_FORMAT_DATETYPE);
            List<ImportantPeopleDTO> importantPeopleDTOSInSevenDays = importantPeopleMapper.getImportantPeopleDTOByDataTime(sevenDaysAgo);
            if (null != importantPeopleDTOSInSevenDays && importantPeopleDTOSInSevenDays.size() > 0) {
                for (int i = 0; i < importantPeopleDTOSInSevenDays.size(); i++) {
                    ImportantPeopleDTO imp = importantPeopleDTOSInSevenDays.get(i);
                    String name = imp.getName();
                    nameSet.add(name);
                    //出入重点人员频次计数
                    if (nameAndTimesMap.get(name) == null) {
                        nameAndTimesMap.put(name, 1);
                    } else {
                        nameAndTimesMap.put(name, nameAndTimesMap.get(name) + 1);
                    }
                    //处理tomcat重的图片，供前端访问
                    String pictureUrl = this.doPictureGenerateInTomcat("picture/important/", imp);
                    imp.setPicture(picturl+"important/"+imp.getSnapUrlPictureNameBak());
                    imp.setPicture2(nameAndFaceUrl.get(name));
                    imp.setEventTime(DPTimeUtil.isoStr2utc8Str(imp.getEventTime(), DPConstant.DATE_FORMAT));

                    importantPeopleDTOList.add(imp);
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        /*2、处理高频人员信息*/
        HighFrequenceResponseDTO responseDTO = new HighFrequenceResponseDTO();
        //高频人员列表信息
        List<HighFrequenceDTO> highFrequenceDTOList = new ArrayList<>();
        for (String name : nameSet) {
            HighFrequenceDTO highFrequenceDTO = new HighFrequenceDTO();
            highFrequenceDTO.setId(null);
            highFrequenceDTO.setName(name);
            highFrequenceDTO.setCode(null);
            highFrequenceDTO.setTimes(nameAndTimesMap.get(name).toString());
            highFrequenceDTO.setPicture(nameAndFaceUrl.get(name));
            highFrequenceDTOList.add(highFrequenceDTO);
        }

        //频次最高人员信息
        Iterator<Map.Entry<String, Integer>> entries = nameAndTimesMap.entrySet().iterator();
        int standardValue = 0;
        String finalName = "";
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            if (entry.getValue() > standardValue) {
                standardValue = entry.getValue();
                finalName = entry.getKey();
            }
        }
        HighestFrequenceDTO highestFrequenceDTO = new HighestFrequenceDTO();
        highestFrequenceDTO.setHighestTimes(nameAndTimesMap.get(finalName) + "");
        highestFrequenceDTO.setHighestName(finalName);
        highestFrequenceDTO.setPicture(nameAndFaceUrl.get(finalName));

        /*3 拼装返回结果*/
        responseDTO.setAllData(highFrequenceDTOList);
        responseDTO.setHighest(highestFrequenceDTO);
        responseDTO.setImportantPeople(importantPeopleDTOList);
        return responseDTO;
    }

    /**
     * 图片信息另存为
     *
     * @param targetPictureUrl
     * @param imp
     * @return
     */
    private String doPictureGenerateInTomcat(String targetPictureUrl, ImportantPeopleDTO imp) {
        String snapUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + targetPictureUrl + imp.getSnapUrlPictureNameBak();
        if (!PictureUtil.checkPictureExisted(snapUrlBak)) {
            JSONObject pictureParam = new JSONObject();
            pictureParam.put("url", imp.getSnapUrl());
            String pactureDown = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_DOWN_, pictureParam);
            JSONObject pictureDownObject = JSONObject.parseObject(pactureDown);
            String pictureData = pictureDownObject.get("data").toString();
            PictureUtil.GenerateImage(pictureData, snapUrlBak);
        }
        return snapUrlBak;
    }

    /**
     * 获取高频使用人员信息
     *
     * @return
     */
    public HighFrequenceResponseDTO getHighFrequenceInfoFromHik() {
        List<ImportantPeopleDTO> importantPeopleDTOList = new ArrayList<>();
        Set<String> nameSet = new HashSet<>();
        Map<String, Integer> nameAndTimes = new HashMap<>();
        Map<String, String> nameAndFaceUrl = this.getStandardNameAndPictureRelation();
        try {
            //调用海康接口"按条件查询重点人员事件"获取数据，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
            String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.get7DaysAgo()), DPConstant.DATE_FORMAT);
            String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(new Date()), DPConstant.DATE_FORMAT);
            String result = this.getImportantPeople(startTime, endTime, 15);
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
                        if (null == nameAndFaceUrl.get(name)) {
                            nameAndFaceUrl.put(name, faceUrl);
                        }

                        if (nameAndTimes.get(name) == null) {
                            nameAndTimes.put(name, 1);
                        } else {
                            nameAndTimes.put(name, nameAndTimes.get(name) + 1);
                        }

                        JSONObject snapObject = jsonArrayList.getJSONObject(i).getJSONObject("snapInfo");
                        JSONObject pictureParam = new JSONObject();
                        pictureParam.put("url", snapObject.getString("snapUrl"));

                        //处理重点人员列表信息
                        String pactureDown = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_DOWN_, pictureParam);
                        JSONObject pictureDownObject = JSONObject.parseObject(pactureDown);
                        String data = pictureDownObject.get("data").toString();
                        String bkgPictureName = UUID.randomUUID().toString() + ".jpg";
                        String bkgUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/important/" + bkgPictureName;
                        if (PictureUtil.GenerateImage(data, bkgUrlBak)) {
                            ImportantPeopleDTO importantPeopleDTO = new ImportantPeopleDTO();
                            importantPeopleDTO.setPicture(picturl + "important/" + bkgPictureName);
                            importantPeopleDTO.setCameraName(snapObject.getString("cameraName"));
                            importantPeopleDTO.setEventTime(DPTimeUtil.isoStr2utc8Str(snapObject.getString("eventTime"), DPConstant.DATE_FORMAT));
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


    /**
     * 查询陌生人员信息，数据为最近七天之内的
     * 包含：最近10条陌生人详情+陌生人占比
     *
     * @return
     */
    @Override
    public StrangerResponseDTO getStrangerInfo() {
        StrangerResponseDTO strangerResponseDTO = new StrangerResponseDTO();

        /*1、查询最近10条陌生人员信息，并处理其图片地址*/
        List<StrangerInfoDTO> strangerInfoDTOList = strangerInfoMapper.getTop10StrangerInfoDTO(new StrangerInfoDTO());
        this.doPictureForStranger(strangerInfoDTOList);

        /*2、查询最近七天的重点人员信息，获取最近七日重点人员数量*/
        String startTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.get7DaysAgo()), DPConstant.DATE_FORMAT);
        String endTime = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(new Date()), DPConstant.DATE_FORMAT);
        String result = this.getImportantPeople(startTime, endTime, 15);
        JSONObject resultObject = JSONObject.parseObject(result);
        int tatalImportant = 0;
        if (null != resultObject.get("msg") && "success".equals(resultObject.get("msg"))) {
            tatalImportant = resultObject.getJSONObject("data").getIntValue("total");
        }

        /*3、查询最近七天的陌生人员数量*/
        int totalStranger = strangerInfoMapper.getAccount(DPTimeUtil.formatDate(DPTimeUtil.get7DaysAgo(), DPConstant.DATE_FORMAT_DATETYPE));
        int rate = 0;
        if (0 != totalStranger + tatalImportant) {
            rate = totalStranger * 100 / (totalStranger + tatalImportant);
        }

        strangerResponseDTO.setAllData(strangerInfoDTOList);
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
        /*1、获取预警类型*/
        List<WarningTypeInfo> warningTypeInfoList = warningTypeMapper.getWarningType();
        int allTypeNum = 0;
        if (null != warningTypeInfoList && warningTypeInfoList.size() > 0) {
            for (int i = 0; i < warningTypeInfoList.size(); i++) {
                int account = warningMapper.selectAmountByType(warningTypeInfoList.get(i).getCode());
                warningTypeInfoList.get(i).setAccount(account);
                allTypeNum = allTypeNum + account;
            }

        }

        if (null != warningTypeInfoList && warningTypeInfoList.size() > 0) {
            List<Integer> num = new ArrayList<>();
            for (int i = 0; i < warningTypeInfoList.size(); i++) {
                int per = 0;
                if (i == warningTypeInfoList.size() - 1) {
                    per = 100;
                    for (int j = 0; j < num.size(); j++) {
                        per = per - num.get(j);
                    }
                } else {
                    per = warningTypeInfoList.get(i).getAccount() * 100 / allTypeNum;
                }
                WarningTypeDTO warningTypeDTO = new WarningTypeDTO(warningTypeInfoList.get(i).getCode(), warningTypeInfoList.get(i).getName(), String.valueOf(per));
                warningTypeDTOList.add(warningTypeDTO);
                num.add(per);
            }

        }

        /*2、获取预警数量*/
        Date today = new Date();
        List<WarningDailyAmountDTO> dailyAmountDTOList = new ArrayList<>();
        int todayAmount = 0;
        int allAmount = 0;
        for (int i = 1; i < 8; i++) {
            String toDayString = DPTimeUtil.formatDate(today, DPConstant.DATE_FORMAT_DATETYPE);
            int amount = warningMapper.selectAmountByDate(toDayString);
            if (i == 1) {
                todayAmount = amount;
            }
            WarningDailyAmountDTO dailyAmountDTO = new WarningDailyAmountDTO();
            dailyAmountDTO.setAmount(String.valueOf(amount));
            dailyAmountDTO.setDate(toDayString);
            dailyAmountDTOList.add(dailyAmountDTO);
            today = DPTimeUtil.getYesterday();
        }
        allAmount = warningMapper.selectAllAmount();

        amountDTO.setTodayAmount(String.valueOf(todayAmount));
        amountDTO.setHistoryAmount(String.valueOf(allAmount));
        amountDTO.setDailyAmount(dailyAmountDTOList);

        /*3、获取预警详情*/
        List<WarningInfo> warningInfoList = warningMapper.getLastWarningData();
        if (null != warningInfoList && warningInfoList.size() > 0) {
            warningInfoList.forEach(item -> {
                warningDetailDTOList.add(new WarningDetailDTO(item.getWarningArea(), WarningTypeConst.warningNameMap.get(item.getWarningType()), item.getWarningContent(), DPTimeUtil.formatDate(item.getCreateTime())));
            });
        }

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
        int healthySensorNum = terminalMapper.getTerminalLAmountByTypeAndStatus("sensor", "1");
        int unHealthySensorNum = terminalMapper.getTerminalLAmountByTypeAndStatus("sensor", "0");
        int healthyCameraNum = terminalMapper.getTerminalLAmountByTypeAndStatus("camera", "1");
        int unHealthyCameraNum = terminalMapper.getTerminalLAmountByTypeAndStatus("camera", "0");
        int all = healthySensorNum + unHealthySensorNum + healthyCameraNum + unHealthyCameraNum;

        TerminalAmountDTO terminalAmountDTO1 = new TerminalAmountDTO(String.valueOf(healthyCameraNum + unHealthyCameraNum), "摄像头", "camera");
        TerminalAmountDTO terminalAmountDTO2 = new TerminalAmountDTO(String.valueOf(healthySensorNum + unHealthySensorNum), "传感器", "sensor");
        terminalAmountDTOList.add(terminalAmountDTO1);
        terminalAmountDTOList.add(terminalAmountDTO2);


        List<TerminalHealthCheckDTO> terminalHealthCheckDTOList = new ArrayList<>();
        TerminalHealthCheckDTO terminalHealthCheckDTO1 = new TerminalHealthCheckDTO("健康摄像头", 100 * healthyCameraNum / all);
        TerminalHealthCheckDTO terminalHealthCheckDTO2 = new TerminalHealthCheckDTO("非健康摄像头", 100 * unHealthyCameraNum / all);
        TerminalHealthCheckDTO terminalHealthCheckDTO3 = new TerminalHealthCheckDTO("健康传感器", 100 * healthySensorNum / all);
        TerminalHealthCheckDTO terminalHealthCheckDTO4 = new TerminalHealthCheckDTO("非健康传感器", 100 - 100 * healthyCameraNum / all - 100 * unHealthyCameraNum / all - 100 * healthySensorNum / all);
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
        List<String> monitorTypeList = new ArrayList<String>() {
            {
                add("gas");
                add("humidity");
                add("temp");
                add("wind");
                add("voicePress");
                add("wave");
            }
        };
        try {
            for (int i = 0; i < monitorTypeList.size(); i++) {
                List<MonitorData> monitorData = monitorDataMapper.getLastMonitorDataByType(monitorTypeList.get(i));
                if (null != monitorData && monitorData.size() > 0) {
                    monitorDataList.addAll(monitorData);
                }
            }
        } catch (Exception e) {
            log.error("查询监控数据时异常", e);
        }
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
        monitorData.setOriginDataTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
        return monitorData;
    }


    /**
     * 处理陌生人图片，使前端能访问
     *
     * @param strangerInfoDTOList
     */
    public void doPictureForStranger(List<StrangerInfoDTO> strangerInfoDTOList) {
        for (int i = 0; i < strangerInfoDTOList.size(); i++) {
            //处理图片地址问题，后期改为fastdfs存储
            String data = PictureUtil.GetImageStr(strangerInfoDTOList.get(i).getSnapUrlBak());
            String bkgPictureName = strangerInfoDTOList.get(i).getSnapUrlPictureNameBak();
            String bkgUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/stranger/" + bkgPictureName;
            PictureUtil.GenerateImage(data, bkgUrlBak);
            strangerInfoDTOList.get(i).setPicture(picturl + "stranger/" + bkgPictureName);


            String backData = PictureUtil.GetImageStr(strangerInfoDTOList.get(i).getBkgUrlBak());
            String backPictureName = strangerInfoDTOList.get(i).getBkgUrlPictureNameBak();
            String backUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/stranger/" + backPictureName;
            PictureUtil.GenerateImage(backData, backUrlBak);
            strangerInfoDTOList.get(i).setBkgUrl(picturl + "stranger/" + backPictureName);

            //times
            strangerInfoDTOList.get(i).setTimes(strangerInfoDTOList.get(i).getTotalSimilar() + "");
            strangerInfoDTOList.get(i).setEventTime(DPTimeUtil.isoStr2utc8Str(strangerInfoDTOList.get(i).getEventTime(), DPConstant.DATE_FORMAT));
        }

    }

    public void doPictureForImportantPeople(List<ImportantPeopleDTO> importantPeopleDTOList) {
        for (int i = 0; i < importantPeopleDTOList.size(); i++) {
            //处理图片地址问题，后期改为fastdfs存储
            String snapPictureName = importantPeopleDTOList.get(i).getSnapUrlPictureNameBak();
            String snapUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/important/" + snapPictureName;

            if (!PictureUtil.checkPictureExisted(snapUrlBak)) {
                String snapPicturedata = PictureUtil.GetImageStr(importantPeopleDTOList.get(i).getSnapUrlBak());
                PictureUtil.GenerateImage(snapPicturedata, snapUrlBak);
            }
            importantPeopleDTOList.get(i).setPicture(picturl + "important/" + snapPictureName);


            String bkgPictureName = importantPeopleDTOList.get(i).getBkgUrlPictureNameBak();
            String bkgUrlBak = this.getClass().getClassLoader().getResource("static").getFile() + "picture/important/" + bkgPictureName;
            if (!PictureUtil.checkPictureExisted(bkgUrlBak)) {
                String bkgPictureData = PictureUtil.GetImageStr(importantPeopleDTOList.get(i).getBkgUrlBak());
                PictureUtil.GenerateImage(bkgPictureData, bkgUrlBak);
            }
            importantPeopleDTOList.get(i).setPicture2(picturl + "stranger/" + bkgPictureName);

            importantPeopleDTOList.get(i).setEventTime(DPTimeUtil.isoStr2utc8Str(importantPeopleDTOList.get(i).getEventTime(), DPConstant.DATE_FORMAT));
        }

    }

    /**
     * 调用海康接口"按条件查询重点人员事件"获取数据，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"
     * 数据查询最近7天
     *
     * @return
     */
    public String getImportantPeople(String startTime, String endTime, int similarity) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray.add("eca9e1993abe4488bacb875fd68e5935");
        jsonObject.put("cameraIndexCodes", jsonArray);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime", endTime);
        jsonObject.put("similarity", similarity);
        jsonObject.put("pageNo", 1);
        jsonObject.put("pageSize", 1000);
        String result = hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_IMPORTANT_, jsonObject);
        return result;
    }

    /**
     * 获取重点人员，人名与头像图片地址关系
     *
     * @return
     */
    public Map<String, String> getStandardNameAndPictureRelation() {
        Map<String, String> nameAndFaceUrl = new HashMap<>();
        if (StringUtils.isNotEmpty(namePictureRelation)) {
            String[] relations = namePictureRelation.split(",");
            if (relations != null && relations.length > 0) {
                for (int i = 0; i < relations.length; i++) {
                    if (relations[i] != null && relations[i].contains("@")) {
                        String[] nameAndUrl = relations[i].split("@");
                        try {
                            String name = new String(nameAndUrl[0].getBytes("ISO8859-1"), "UTF-8");
                            nameAndFaceUrl.put(name, picturl + "standard/" + nameAndUrl[1]);
                        } catch (UnsupportedEncodingException e) {
                            log.error("getStandardNameAndPictureRelation error when encode", e);
                        }
                    }
                }
            }
        }
        return nameAndFaceUrl;
    }
}
