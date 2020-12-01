package com.bang.ap.dp.receive.controller;

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
    @RequestMapping(path = "/subscribe", method = RequestMethod.GET)
    @ResponseBody
    public String subscribeEventByType(HttpServletRequest request) {
        String eventType=request.getParameter("eventType");
        String eventDest=request.getParameter("eventDest");
        JSONObject jsonObject=new JSONObject();
        JSONArray  eventTypeJsonArray=new JSONArray();
        eventTypeJsonArray.add(eventType);
        jsonObject.put("eventType",eventTypeJsonArray);
        jsonObject.put("eventDest",eventDest);
        jsonObject.put("subType",0);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_EVENT_SUBSCRIBE,jsonObject);
        log.info("按照事件类型订阅事件，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_EVENT_SUBSCRIBE,jsonObject.toJSONString(),request);
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

}
