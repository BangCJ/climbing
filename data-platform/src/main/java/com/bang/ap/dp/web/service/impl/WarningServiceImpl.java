package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.web.entity.UserInfo;
import com.bang.ap.dp.web.entity.WarningInfo;
import com.bang.ap.dp.web.mapper.WarningMapper;
import com.bang.ap.dp.web.service.WarningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WarningServiceImpl implements WarningService {

    @Autowired
    private WarningMapper warningMapper;

    @Override
    public WarningInfo getWarningInfoById(int id) {
        WarningInfo warningInfo = warningMapper.selectById(id);
        return warningInfo;
    }

    @Override
    public void addWarningInfo(WarningInfo warningInfo) {
        warningMapper.addWarningInfo(warningInfo);
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
    private PageInfo<WarningInfo> getPageInfo(PageRequest pageRequest, Map<String, Object> searchMap) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<WarningInfo> warningInfoList = warningMapper.selectPage(searchMap);
        return new PageInfo<WarningInfo>(warningInfoList);
    }

}
