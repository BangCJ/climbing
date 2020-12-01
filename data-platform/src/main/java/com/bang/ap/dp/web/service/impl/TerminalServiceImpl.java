package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.web.entity.TerminalInfo;
import com.bang.ap.dp.web.entity.WarningInfo;
import com.bang.ap.dp.web.mapper.TerminalMapper;
import com.bang.ap.dp.web.mapper.WarningMapper;
import com.bang.ap.dp.web.service.TerminalService;
import com.bang.ap.dp.web.service.WarningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.ir.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TerminalServiceImpl implements TerminalService {

    @Autowired
    private TerminalMapper terminalMapper;

    @Override
    public TerminalInfo getTerminalInfoById(int id) {
        TerminalInfo terminalInfo = terminalMapper.selectById(id);
        return terminalInfo;
    }

    @Override
    public void addTerminalInfo(TerminalInfo terminalInfo) {
        terminalMapper.addTerminalInfo(terminalInfo);

    }

    @Override
    public void updateTerminalInfo(TerminalInfo terminalInfo) {
        terminalMapper.updateTerminalInfo(terminalInfo);

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
    private PageInfo<TerminalInfo> getPageInfo(PageRequest pageRequest, Map<String, Object> searchMap) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<TerminalInfo> terminalInfoList = terminalMapper.selectPage(searchMap);
        return new PageInfo<TerminalInfo>(terminalInfoList);
    }


}
