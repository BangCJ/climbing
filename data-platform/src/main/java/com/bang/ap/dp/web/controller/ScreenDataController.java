package com.bang.ap.dp.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.dto.*;
import com.bang.ap.dp.analysis.service.DataAnalysisService;
import com.bang.ap.dp.utils.ResponseUtil;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/screen")
@Slf4j
public class ScreenDataController {

    @Autowired
    private DataAnalysisService dataAnalysisService;

    @RequestMapping(path = "/timesOfRoom/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject timesOfRoom(@PathVariable String id) {
        try {
            List<FrequenceInRoomDTO> frequenceList = dataAnalysisService.getFrequenceOfRoomInOneWeek(id);
            if (null == frequenceList || frequenceList.size() < 0) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            return ResponseUtil.buildSuccessResponse(frequenceList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

    @RequestMapping(path = "/timeOfRoom/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject timeOfRoom(@PathVariable String id) {
        JSONObject response ;
        try {
            List<RoomUseTimeDTO> frequenceList = dataAnalysisService.getRoomUsedTimeInOneWeek(id);
            int rate = dataAnalysisService.getRooUserdRateInOneWeek(id);
            if (null == frequenceList || frequenceList.size() < 0) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(frequenceList);
            response.put("rate", rate);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

    @RequestMapping(path = "/highFrequencyPeople", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject highFrequencyPeople() {
        JSONObject response ;
        try {
            HighFrequenceResponseDTO highFrequenceResponseDTO = dataAnalysisService.getHighFrequenceInfo();
            if (null == highFrequenceResponseDTO ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(highFrequenceResponseDTO);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

    @RequestMapping(path = "/stranger", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject stranger() {
        JSONObject response ;
        try {
            StrangerResponseDTO strangerResponseDTO = dataAnalysisService.getStrangerInfo();
            if (null == strangerResponseDTO ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(strangerResponseDTO);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }


    @RequestMapping(path = "/bispectrum", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject bispectrum() {
        JSONObject response ;
        try {
            String bispectrumUrl = dataAnalysisService.getBispectrumUrl();
            if (null == bispectrumUrl ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(bispectrumUrl);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

    @RequestMapping(path = "/warningInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject warningInfo() {
        JSONObject response ;
        try {
            WarningResponseDTO warningResponseDTO = dataAnalysisService.getWarningInfo();
            if (null == warningResponseDTO ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(warningResponseDTO);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }



    @RequestMapping(path = "/dataChangeForFire", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject dataChangeForFire() {
        JSONObject response ;
        try {
            List<DataChangeDTO> dataChangeDTO = dataAnalysisService.getDataChangeForFire();
            if (null == dataChangeDTO ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(dataChangeDTO);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

    @RequestMapping(path = "/realTimeDataForFire", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject realTimeDataForFire() {
        JSONObject response ;
        try {
            List<DataRealTimeDTO> dataRealTimeDTOS = dataAnalysisService.getRealTimeDataForFire();
            if (null == dataRealTimeDTOS ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(dataRealTimeDTOS);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

    //
    @RequestMapping(path = "/terminalInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject terminalInfo() {
        JSONObject response ;
        try {
            TerminalResponseDTO terminalResponseDTO = dataAnalysisService.getTerminalInfo();
            if (null == terminalResponseDTO ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(terminalResponseDTO);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }



    @RequestMapping(path = "/warningDataInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject warningDataInfo() {
        JSONObject response ;
        try {
            List<MonitorData> monitorDataList = dataAnalysisService.getMonitorData();
            if (null == monitorDataList ) {
                return ResponseUtil.buildFailureResponse("查询数据为空");
            }
            response = ResponseUtil.buildSuccessResponse(monitorDataList);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

}
