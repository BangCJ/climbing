package com.bang.ap.dp.web.controller;


import com.bang.ap.dp.socket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class BaseController {

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


}
