package com.bang.ap.dp.receive.hikvision.controller;

import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.utils.PictureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/face")
@Slf4j
public class FaceController {
    @Autowired
    HikvisionUtil hikvisionUtil;


    /**
     * 按条件查询人脸抓拍事件
     * @return
     */
    @RequestMapping(path = "/listNormalFaceEvent", method = RequestMethod.GET)
    @ResponseBody
    public String listNormalFaceEvent(HttpServletRequest request) {
        String pageNo=request.getParameter("pageNo");
        String pageSize=request.getParameter("pageSize");
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("pageNo",1);
        jsonObject.put("pageSize",20);
        jsonObject.put("startTime","2020-12-07T17:53:11.484+08:00");
        jsonObject.put("endTime","2020-12-07T17:30:08.000+08:00");
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_NORMAL_,jsonObject);
        log.debug("按条件查询人脸抓拍事件，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_FACE_EVENT_NORMAL_,jsonObject.toJSONString(),request);
        return result;

    }
    /**
     * 按条件查询重点人员事件
     * @return
     */
    @RequestMapping(path = "/listImportantFaceEvent", method = RequestMethod.GET)
    @ResponseBody
    public String listImportantFaceEvent(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String pageNo=request.getParameter("pageNo");
        String pageSize=request.getParameter("pageSize");

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("pageNo",pageNo);
        jsonObject.put("pageSize",pageSize);
        jsonObject.put("startTime",startTime);
        jsonObject.put("endTime",endTime);
        jsonObject.put("similarity",80);
        jsonObject.put("endTime",endTime);
        jsonObject.put("endTime",endTime);
        jsonObject.put("endTime",endTime);

        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_IMPORTANT_,jsonObject);
        log.debug("按条件查询重点人员事件，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_FACE_EVENT_IMPORTANT_,jsonObject.toJSONString(),request);
        return result;
    }

    /**
     * 按条件查询陌生人事件
     * @return
     */
    @RequestMapping(path = "/listStrangeFaceEvent", method = RequestMethod.GET)
    @ResponseBody
    public String listStrangeFaceEvent(HttpServletRequest request) {
        JSONObject jsonObject=new JSONObject();

        String endTime=request.getParameter("endTime");
        String startTime=request.getParameter("startTime");
        jsonObject.put("startTime",startTime);
        jsonObject.put("endTime",endTime);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_STRANGE_,jsonObject);
        log.debug("按条件查询陌生人事件，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_FACE_EVENT_STRANGE_,jsonObject.toJSONString(),request);
        return result;
    }

    /**
     * 按条件查询高频人员识别事件
     * @return
     */
    @RequestMapping(path = "/listHighFrequencyFaceEvent", method = RequestMethod.GET)
    @ResponseBody
    public String listHighFrequencyFaceEvent(HttpServletRequest request) {
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("startTime",startTime);
        jsonObject.put("endTime",endTime);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_HITHFREQUENCY_,jsonObject);
        log.debug("按条件查询陌生人事件，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_FACE_EVENT_HITHFREQUENCY_,jsonObject.toJSONString(),request);
        return result;
    }



    /**
     * 接口回调，重点人员识别事件
     * @param jsonObject
     * @return
     */
    @RequestMapping(path = "/eventCallback/importantPerson")
    @ResponseBody
    public String importantPersonCallback(@RequestBody JSONObject jsonObject) {
        log.debug("收到回调信息，重点人员识别事件。接受参数为:{}",jsonObject.toJSONString());
        return "success";
    }

    /**
     * 接口回调，人脸抓拍事件
     * @param jsonObject
     * @return
     */
    @RequestMapping(path = "/eventCallback/normalPerson")
    @ResponseBody
    public String normalPersonCallback(@RequestBody JSONObject jsonObject) {
        log.debug("收到回调信息，人脸抓拍事件。接受参数为:{}",jsonObject.toJSONString());
        return "success";
    }

    /**
     * 接口回调，陌生人识别事件
     * @param jsonObject
     * @return
     */
    @RequestMapping(path = "/enentCallback/stranger")
    @ResponseBody
    public String strangerCallback(@RequestBody JSONObject jsonObject) {
        log.debug("收到回调信息，陌生人识别事件。接受参数为:{}",jsonObject.toJSONString());
        return "success";
    }


    @RequestMapping(path = "/picturlDown", method = RequestMethod.POST)
    @ResponseBody
    public String getPreviewURLs(@RequestBody JSONObject jsonObject) {


        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_DOWN_,jsonObject);
        JSONObject resultObject= JSONObject.parseObject(result);
        String data=resultObject.get("data").toString();
        PictureUtil.GenerateImage(data,"/Users/chenjianbang/Downloads/11.jpg");
        return result;
    }


    @RequestMapping(path = "/captureSearch", method = RequestMethod.POST)
    @ResponseBody
    public String captureSearch(@RequestBody JSONObject jsonObject) {
        String facePicBinaryData=PictureUtil.GetImageStr("/Users/chenjianbang/Downloads/33.jpeg");
        jsonObject.put("facePicBinaryData",facePicBinaryData);

        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_PICTURE_CAPTURESEARCH,jsonObject);
        JSONObject resultObject= JSONObject.parseObject(result);

        return result;
    }


}
