package com.bang.ap.dp.receive.hikvision.controller;

import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.HikvisionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@Slf4j
public class VideoReceiveController {
    @Autowired
    HikvisionUtil hikvisionUtil;

    /**
     * 分页获取监控点资源
     * @return
     */
    @RequestMapping(path = "/camera/list", method = RequestMethod.GET)
    @ResponseBody
    public String getCameraList() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("pageNo",1);
        jsonObject.put("pageSize",10);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_VIDEO_CAMERA_PAGE,jsonObject);
        log.info(result);
        return result;


    }

    /**
     *获取监控点预览取流URLv2
     * @param id
     * @return
     */
    @RequestMapping(path = "/previewURLs/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getPreviewURLs(@PathVariable String id) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("transmode",1);
        jsonObject.put("cameraIndexCode",id);
        jsonObject.put("streamType",1);
        jsonObject.put("protocol","hls");
        jsonObject.put("streamform","ps");
        jsonObject.put("expand","transcode=1&resolution=D1&bitrate=512&framerate=15&videotype=h264&systemformat=rtp&audiotype=AAC&samples=8000&audiobitrate=64000&bitspersample=16");
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_VIDEO_PREVIEW,jsonObject);
        return result;
    }

    /**
     * 获取监控点回放取流URLv2
     * @param id
     * @return
     */
    @RequestMapping(path = "/playbackURLs/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getPlaybackURLs(@PathVariable String id) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("cameraIndexCode",id);
        jsonObject.put("recordLocation",0);
        jsonObject.put("protocol","rtsp");
        jsonObject.put("transmode",0);
        jsonObject.put("beginTime","2020-12-07T15:00:00.000+08:00");
        jsonObject.put("endTime","2017-12-07T16:00:00.000+08:00");
        jsonObject.put("uuid","");
        jsonObject.put("streamform","ps");
        jsonObject.put("lockType",0);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_VIDEO_PALYBACK,jsonObject);
        return result;
    }


    /**
     * 查询编码设备列表v2
     * @return
     */
    @RequestMapping(path = "/encodeDevice/list", method = RequestMethod.GET)
    @ResponseBody
    public String getEncodeDeviceList() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("pageNo",1);
        jsonObject.put("pageSize",10);
        String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_VIDEO_ENCODEDEVICE,jsonObject);
        log.info(result);
        return result;


    }
}
