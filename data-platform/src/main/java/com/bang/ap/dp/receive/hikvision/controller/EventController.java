package com.bang.ap.dp.receive.hikvision.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.HikvisionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/event")
@Slf4j
public class EventController {
    @Autowired
    HikvisionUtil hikvisionUtil;

    /**
     * 按事件类型订阅事件
     * @return
     */
    @RequestMapping(path = "/subscribe", method = RequestMethod.POST)
    @ResponseBody
    public String subscribeEventByType(@RequestBody JSONObject param) {

        int eventType=param.getInteger("eventType");
        String eventDest=param.getString("eventDest");
        JSONObject jsonObject=new JSONObject();
        JSONArray  eventTypeJsonArray=new JSONArray();
        eventTypeJsonArray.add(eventType);
        jsonObject.put("eventTypes",eventTypeJsonArray);
        jsonObject.put("eventDest",eventDest);
        jsonObject.put("subType",0);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_EVENT_SUBSCRIBE,jsonObject);
        log.info("按照事件类型订阅事件，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_EVENT_SUBSCRIBE,jsonObject.toJSONString(),result);
        return result;

    }
    /**
     * 按事件类型取消订阅
     */
    @RequestMapping(path = "/unSubscribe", method = RequestMethod.GET)
    @ResponseBody
    public String unSubscribeEventByType(HttpServletRequest request) {
        String eventType=request.getParameter("eventType");
        JSONObject jsonObject=new JSONObject();
        JSONArray  eventTypeJsonArray=new JSONArray();
        eventTypeJsonArray.add(eventType);
        jsonObject.put("eventType",eventTypeJsonArray);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_EVENT_UNSUBSCRIBE_,jsonObject);
        log.info("按事件类型取消订阅，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_EVENT_UNSUBSCRIBE_,jsonObject.toJSONString(),request);
        return result;
    }

    /**
     * 查询事件订阅信息
     * @return
     */
    @RequestMapping(path = "/listSbuscribedEvent", method = RequestMethod.GET)
    @ResponseBody
    public String listSbuscribedEvent(HttpServletRequest request) {

        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_EVENT_SUBSCRIBED_LIST_,new JSONObject());
        log.info("查询事件订阅信息，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_EVENT_SUBSCRIBED_LIST_,"空",request);
        return result;

    }

    /**
     * 事件回调
     * @return
     */
    @RequestMapping(path = "/onEvent/intrusionWarning")
    @ResponseBody
    public String intrusionWarning(HttpServletRequest request) {
        log.info("接收到事件/onEvent/intrusionWarning回调信息:");

        return "success";

    }

}
