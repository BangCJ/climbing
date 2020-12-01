package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.web.entity.RoomInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.RoomMapper;
import com.bang.ap.dp.web.mapper.UserMapper;
import com.bang.ap.dp.web.service.RoomService;
import com.bang.ap.dp.web.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomMapper roomMapper;


    @Override
    public RoomInfo getRoomInfoById(int id) {
        return roomMapper.selectById(id);
    }

    @Override
    public void addRoomInfo(RoomInfo roomInfo) {
        roomMapper.addRoomInfo(roomInfo);

    }

    @Override
    public void updateRoomInfo(RoomInfo roomInfo) {
        roomMapper.updateRoomInfo(roomInfo);

    }

    @Override
    public List<RoomInfo> getRoomList() {
        return roomMapper.getRoomList();
    }

    @Override
    public PageResult findPage(PageRequest pageRequest, Map<String, Object> searchMap) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest,searchMap));
    }

    /**
     * 调用分页插件完成分页
     * @param
     * @return
     */
    private PageInfo<RoomInfo> getPageInfo(PageRequest pageRequest, Map<String, Object> searchMap) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<RoomInfo> roomInfoList = roomMapper.selectPage(searchMap);
        return new PageInfo<RoomInfo>(roomInfoList);
    }

}
