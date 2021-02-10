package com.bang.ap.dp.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.service.DataPesistenceService;
import com.bang.ap.dp.socket.WebSocketServer;
import com.bang.ap.dp.utils.ResponseUtil;
import com.bang.ap.dp.web.entity.RoomInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BaseController {
    @Autowired
    private DataPesistenceService dataPesistenceService ;

    @RequestMapping(value = "/index/{userId}")
    public String IM(ModelMap modelMap, @PathVariable String userId){
        modelMap.put("userId",userId);
        return "IM";
    }

    @RequestMapping(value="/pushListToWeb")
    @ResponseBody
    public Map<String,Object> pushVideoListToWeb(String cid, String message) {
        Map<String,Object> result =new HashMap<>();

        try {
            if (message==null){
                message="defalut message ";
            }
            WebSocketServer.sendInfo(message,cid);
            result.put("operationResult", true);
        }catch (Exception e) {
            result.put("operationResult", true);
        }
        return result;
    }


    @RequestMapping(path = "/runScheduleTask", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject runScheduleTask() {
        try {
            //do things about frequence
            //dataPesistenceService.saveFrequenceInRoom(new Date());
            //do things about used time length info
            //dataPesistenceService.saveRoomUseTimeLength(new Date());
            //do things about stranger
           // dataPesistenceService.saveStrangerInfo(new Date());
            //do things about stranger
            dataPesistenceService.saveImportantPeopleInfo(new Date());
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.buildFailureResponse(e.getMessage());
        }
    }

}
