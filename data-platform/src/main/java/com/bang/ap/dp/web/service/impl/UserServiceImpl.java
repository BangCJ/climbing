package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.utils.ValidateUtil;
import com.bang.ap.dp.web.entity.PwdInfo;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.mapper.PwdMapper;
import com.bang.ap.dp.web.mapper.UserMapper;
import com.bang.ap.dp.web.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PwdMapper pwdMapper;

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
    public List<UserInfo> getUserByUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserInfo(UserInfo userInfo) {
        userMapper.addUserInfo(userInfo);
        PwdInfo pwdInfo=new PwdInfo();
        pwdInfo.setUserId(userInfo.getId());
        pwdInfo.setUserCode(userInfo.getCode());
        pwdInfo.setPwd(DPConstant.DEFAULT_PWD);
        pwdInfo.setCreateTime(new Date());
        pwdInfo.setUpdateTime(new Date());
        pwdMapper.addPwdInfo(pwdInfo);

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

    @Override
    public boolean checkRepeat(UserInfo userInfo) throws Exception {
        if (StringUtils.isNotEmpty(userInfo.getCode())){
            UserInfo codeUser=new UserInfo();
            codeUser.setCode(userInfo.getCode());
            List<UserInfo>codeUserList=userMapper.getUserByUserInfo(codeUser);
            if (codeUserList !=null && codeUserList.size()>0){
                throw  new Exception("用户编码重复");
            }
        }

        if (StringUtils.isNotEmpty(userInfo.getEmail())){
            if (!ValidateUtil.isEmailLegal(userInfo.getEmail())){
                throw  new Exception("用户邮箱格式非法");
            }
            UserInfo emailUser=new UserInfo();
            emailUser.setEmail(userInfo.getEmail());
            List<UserInfo>emailUserList=userMapper.getUserByUserInfo(emailUser);
            if (emailUserList !=null && emailUserList.size()>0){
                throw  new Exception("用户邮箱重复重复");
            }
        }

        if (StringUtils.isNotEmpty(userInfo.getPhone())){
            if (!ValidateUtil.isChinaPhoneLegal(userInfo.getPhone())){
                throw  new Exception("用户手机号格式非法");
            }
            UserInfo phoneUser=new UserInfo();
            phoneUser.setPhone(userInfo.getPhone());
            List<UserInfo>phoneUserList=userMapper.getUserByUserInfo(phoneUser);
            if (phoneUserList !=null && phoneUserList.size()>0){
                throw  new Exception("用户手机号重复");
            }
        }


        return true;
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
