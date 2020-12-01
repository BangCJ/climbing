package com.bang.ap.dp.web.service;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.web.entity.RoomInfo;
import com.bang.ap.dp.web.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface RoomService {

    RoomInfo getRoomInfoById(int id);

    void addRoomInfo(RoomInfo roomInfo);

    void updateRoomInfo(RoomInfo roomInfo);

    List<RoomInfo> getRoomList();

    PageResult findPage(PageRequest pageRequest, Map<String, Object> searchMap);



}
