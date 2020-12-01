package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.UserMapper;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserInfo getUserById(int id) {
        UserInfo user = (UserInfo) redisTemplate.opsForValue().get("userObj");
        if (null != user) {
            return user;
        }
        user = userMapper.selectById(id);
        redisTemplate.opsForValue().set("userObj", user,10, TimeUnit.MINUTES);
        return user;
    }

    @Override
    public void addUserInfo(UserInfo userInfo) {
        userMapper.addUserInfo(userInfo);
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        userMapper.updateUserInfo(userInfo);
    }

    @Override
    public List<UserInfo> getUserList() {
        List<UserInfo>userInfoList=userMapper.getUserList();
        return userInfoList;
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
    private PageInfo<UserInfo> getPageInfo(PageRequest pageRequest,Map<String, Object> searchMap) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<UserInfo> userInfoList = userMapper.selectPage(searchMap);
        return new PageInfo<UserInfo>(userInfoList);
    }

}
