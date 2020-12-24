package com.bang.ap.dp.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bang.ap.dp.analysis.dto.FrequenceInRoomDTO;
import com.bang.ap.dp.analysis.dto.RoomUseTimeDTO;
import com.bang.ap.dp.analysis.service.DataPesistenceService;
import com.bang.ap.dp.cons.UrlConstant;
import com.bang.ap.dp.utils.HikvisionUtil;
import com.bang.ap.dp.web.mapper.FrequenceInRoomMapper;
import com.bang.ap.dp.web.mapper.RoomUsedTimeLengthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@Slf4j
public class DataPersistenceServiceImpl implements DataPesistenceService {

    @Autowired
    HikvisionUtil hikvisionUtil;

    @Autowired
    FrequenceInRoomMapper frequenceInRoomMapper;


    @Autowired
    private RoomUsedTimeLengthMapper roomUsedTimeLengthMapper;

    @Override
    public void saveFrequenceInRoom(Date date) {
        //1、调用海康接口获取，获取人脸抓拍事件，指定摄像机"A300人脸抓拍" "cameraIndexcode"="eca9e1993abe4488bacb875fd68e5935"，

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("pageNo",1);
            jsonObject.put("pageSize",1000);
            jsonObject.put("startTime","2020-12-07T17:53:11.484+08:00");
            jsonObject.put("endTime","2020-12-07T17:30:08.000+08:00");
            String result=hikvisionUtil.getDataFromHikvision(UrlConstant.URL_FACE_EVENT_NORMAL_,jsonObject);
            log.info("按条件查询人脸抓拍事件，请求url={},请求参数为{},请求结果为{}",UrlConstant.URL_FACE_EVENT_NORMAL_,jsonObject.toJSONString(),result);




        //2、保存数据
         frequenceInRoomMapper.insertFrequenceInRoomDTO(new FrequenceInRoomDTO());


    }

    @Override
    public void saveRoomUseTimeLength(Date date) {

        //1、调用海康接口获取


        //2、保存数据
        roomUsedTimeLengthMapper.insertRoomUseTimeDTO(new RoomUseTimeDTO());

    }
}
