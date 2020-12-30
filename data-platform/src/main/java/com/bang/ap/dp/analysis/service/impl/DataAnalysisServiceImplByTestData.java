package com.bang.ap.dp.analysis.service.impl;

import com.bang.ap.dp.analysis.dto.*;
import com.bang.ap.dp.analysis.service.DataAnalysisService;
import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@ConditionalOnProperty(prefix = "ap", name = "test-enable", havingValue = "true", matchIfMissing = false)
public class DataAnalysisServiceImplByTestData implements DataAnalysisService {
    @Override
    public List<FrequenceInRoomDTO> getFrequenceOfRoomInOneWeek(String id) {
        Date today = new Date();
        List<FrequenceInRoomDTO> frequenceInRoomDTOS=new ArrayList<>();
        for (int i = 1; i < 8 ; i++) {
            FrequenceInRoomDTO frequenceInRoomDTO=new FrequenceInRoomDTO();
            frequenceInRoomDTO.setRoomId(i);
            frequenceInRoomDTO.setTimes(i);
            frequenceInRoomDTO.setCheckDate(DPTimeUtil.formatDate(today, DPConstant.DATE_FORMAT_DATETYPE));
            frequenceInRoomDTOS.add(frequenceInRoomDTO);
            today=DPTimeUtil.getYesterday();

        }
        return frequenceInRoomDTOS;
    }

    @Override
    public List<RoomUseTimeDTO> getRoomUsedTimeInOneWeek(int id) {
        Date today = new Date();
        List<RoomUseTimeDTO> roomUseTimeDTOS=new ArrayList<>();
        for (int i = 1; i <8 ; i++) {
            RoomUseTimeDTO roomUseTimeDTO=new RoomUseTimeDTO();
            roomUseTimeDTO.setId(i);
            roomUseTimeDTO.setTimeLength(i+10);
            roomUseTimeDTO.setDate(DPTimeUtil.formatDate(today, DPConstant.DATE_FORMAT_DATETYPE));
            roomUseTimeDTOS.add(roomUseTimeDTO);
            today=DPTimeUtil.getYesterday();

        }
        return roomUseTimeDTOS;
    }

    @Override
    public int getRooUserdRateInOneWeek(int id) {
        return 65;
    }

    @Override
    public HighFrequenceResponseDTO getHighFrequenceInfo() {
        HighFrequenceResponseDTO responseDTO=new HighFrequenceResponseDTO();
        List<HighFrequenceDTO>highFrequenceDTOList =new ArrayList<>();
        for (int i = 1; i <5 ; i++) {
            HighFrequenceDTO highFrequenceDTO=new HighFrequenceDTO();
            highFrequenceDTO.setId(String.valueOf(i));
            highFrequenceDTO.setCode(String.valueOf(i));
            highFrequenceDTO.setName("张"+i);
            highFrequenceDTO.setTimes(String.valueOf(i));
            highFrequenceDTOList.add(highFrequenceDTO);

        }
        HighestFrequenceDTO highestFrequenceDTO=new HighestFrequenceDTO();
        highestFrequenceDTO.setHighestName("张5");
        highestFrequenceDTO.setHighestTimes("5");
        highestFrequenceDTO.setPicture("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1979722641,1482199216&fm=26&gp=0.jpg");

        responseDTO.setAllData(highFrequenceDTOList);
        responseDTO.setHighest(highestFrequenceDTO);
        return responseDTO;
    }

    @Override
    public StrangerResponseDTO getStrangerInfo() {
        StrangerResponseDTO strangerResponseDTO=new StrangerResponseDTO();
        List<StrangerInfoDTO>strangerInfoDTOList =new ArrayList<>();
        List<String>pictureUrlList=new ArrayList<>();
        pictureUrlList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=334095196,2262933056&fm=26&gp=0.jpg");
        pictureUrlList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=773135270,3837617280&fm=26&gp=0.jpg");
        pictureUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606622684660&di=854355d14bde293558e05d92347d90bd&imgtype=0&src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi4%2F3083885855%2FTB2yfW_cFooBKNjSZPhXXc2CXXa_%2521%25213083885855.jpg_500x500q90.jpg");
        for (int i = 1; i <4 ; i++) {
            StrangerInfoDTO strangerInfoDTO=new StrangerInfoDTO();
            strangerInfoDTO.setId(String.valueOf(i));
            strangerInfoDTO.setCode(String.valueOf(i));
            strangerInfoDTO.setName("陌生人"+i);
            strangerInfoDTO.setTimes(String.valueOf(i));
            strangerInfoDTO.setPicture(pictureUrlList.get(i-1));
            strangerInfoDTO.setDataTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
            strangerInfoDTOList.add(strangerInfoDTO);

        }
        strangerResponseDTO.setAllData(strangerInfoDTOList);
        strangerResponseDTO.setRate("40");
        return strangerResponseDTO;
    }

    @Override
    public String getBispectrumUrl() {
        return "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4107912782,1042503672&fm=26&gp=0.jpg";
    }

    @Override
    public WarningResponseDTO getWarningInfo() {
        WarningResponseDTO responseDTO=new WarningResponseDTO();
        WarningAmountDTO amountDTO=new WarningAmountDTO();
        List<WarningTypeDTO> warningTypeDTOList =new ArrayList<>();
        List<WarningDetailDTO>warningDetailDTOList =new ArrayList<>();
        List<WarningDailyAmountDTO>dailyAmountDTOS=new ArrayList<>();

        WarningTypeDTO warningTypeDTO1=new WarningTypeDTO("yuejie","越界预警","10");
        WarningTypeDTO warningTypeDTO2=new WarningTypeDTO("chuanganqi","传感器预警","70");
        WarningTypeDTO warningTypeDTO3=new WarningTypeDTO("qushi","趋势预警","10");
        WarningTypeDTO warningTypeDTO4=new WarningTypeDTO("other","其他预警","10");
        warningTypeDTOList.add(warningTypeDTO1);
        warningTypeDTOList.add(warningTypeDTO2);
        warningTypeDTOList.add(warningTypeDTO3);
        warningTypeDTOList.add(warningTypeDTO4);

        for (int i = 0; i < 7 ; i++) {
            WarningDetailDTO detailDTO=new WarningDetailDTO();
            detailDTO.setArea("实验室301");
            detailDTO.setTime(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT));
            detailDTO.setWarningInfo("实验室温度当前值为4"+i+"度，超过阈值");
            detailDTO.setWarningType("传感器预警");
            warningDetailDTOList.add(detailDTO);
        }

        Date today = new Date();
        List<WarningDailyAmountDTO> dailyAmountDTOList=new ArrayList<>();
        for (int i = 1; i < 8 ; i++) {
            WarningDailyAmountDTO dailyAmountDTO=new WarningDailyAmountDTO();
            dailyAmountDTO.setAmount(String.valueOf(i));
            dailyAmountDTO.setDate(DPTimeUtil.formatDate(today, DPConstant.DATE_FORMAT_DATETYPE));
            dailyAmountDTOList.add(dailyAmountDTO);
            today=DPTimeUtil.getYesterday();
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
    public List<DataChangeDTO>  getDataChangeForFire() {
        List<DataChangeDTO> response=new ArrayList<>();
        DataChangeDTO dataChangeDTO1=new DataChangeDTO(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT),"1","1","1");

        response.add(dataChangeDTO1);

        return response;
    }

    @Override
    public List<DataRealTimeDTO> getRealTimeDataForFire() {
        List<DataRealTimeDTO> response=new ArrayList<>();
        DataRealTimeDTO dataRealTimeDTO1=new DataRealTimeDTO(DPTimeUtil.getCurrentLocalDateTime(DPConstant.DATE_FORMAT),"11","12","11","11","12","11");

        response.add(dataRealTimeDTO1);

        return response;
    }

    @Override
    public TerminalResponseDTO getTerminalInfo() {
        TerminalResponseDTO responseDTO=new TerminalResponseDTO();
        List<TerminalAmountDTO>terminalAmountDTOList =new ArrayList<>();
        TerminalHealthCheckDTO healthCheckDTO =new TerminalHealthCheckDTO();
        TerminalAmountDTO terminalAmountDTO1 =new TerminalAmountDTO("10","摄像头","camera");
        TerminalAmountDTO terminalAmountDTO2 =new TerminalAmountDTO("20","传感器","sensor");
        terminalAmountDTOList.add(terminalAmountDTO1);
        terminalAmountDTOList.add(terminalAmountDTO2);


        List<TerminalHealthCheckDTO> terminalHealthCheckDTOList =new ArrayList<>();
        TerminalHealthCheckDTO terminalHealthCheckDTO1=new TerminalHealthCheckDTO("健康摄像头",10);
        TerminalHealthCheckDTO terminalHealthCheckDTO2=new TerminalHealthCheckDTO("非健康摄像头",30);
        TerminalHealthCheckDTO terminalHealthCheckDTO3=new TerminalHealthCheckDTO("健康传感器",20);
        TerminalHealthCheckDTO terminalHealthCheckDTO4=new TerminalHealthCheckDTO("非健康传感器",40);
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
        List<MonitorData> monitorDataList =new ArrayList<>();
        MonitorData monitorData=new MonitorData();
        MonitorData monitorData2=new MonitorData();
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
        MonitorData monitorData=new MonitorData();
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
