package com.bang.ap.dp.web.service.impl;


import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.DPTimeUtil;
import com.bang.ap.dp.utils.PageRequest;
import com.bang.ap.dp.utils.PageResult;
import com.bang.ap.dp.utils.PageUtils;
import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.entity.MonitorDataThreshold;
import com.bang.ap.dp.web.entity.WarningInfo;
import com.bang.ap.dp.web.mapper.MonitorDataThresholdMapper;
import com.bang.ap.dp.web.mapper.WarningMapper;
import com.bang.ap.dp.web.service.MessageService;
import com.bang.ap.dp.web.service.WarningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WarningServiceImpl implements WarningService {

    @Autowired
    private WarningMapper warningMapper;

    @Autowired
    private MonitorDataThresholdMapper monitorDataThresholdMapper;

    @Autowired
    private MessageService messageService;


    static final Map<String, String> roomAndMonitorTypeRelation = ImmutableMap.<String, String>builder()
            .put("gas", "实验室300")
            .put("humidity", "实验室300")
            .put("temp", "实验室300")
            .put("wind", "实验室300")
            .put("voicePress", "实验室309")
            .put("wave", "实验室309")
            .put("residualCurrent", "实验室309")
            .put("electricalTemperature", "实验室309")
            .build();

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
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest, searchMap));
    }

    @Override
    public void checkDateForWarning(MonitorData monitorData) {
        String monitorType = monitorData.getMonitorType();
        String monitorValue = monitorData.getValue();
        MonitorDataThreshold monitorDataThreshold = monitorDataThresholdMapper.getThresholdByType(monitorType);
        if (monitorDataThreshold == null) {
            log.error("缺少{}的阈值", monitorValue);
            return;
        }

        String standArdValue = monitorDataThreshold.getValue();
        if (Double.valueOf(monitorValue) > Double.valueOf(standArdValue)) {
            WarningInfo warningInfo = new WarningInfo();
            warningInfo.setCode(monitorData.getCode());
            warningInfo.setWarningArea(roomAndMonitorTypeRelation.get(monitorData.getMonitorType()));
            warningInfo.setWarningType("sensorWarning");
            warningInfo.setWarningTime(DPTimeUtil.formatDate(DPTimeUtil.getNowDate(), DPConstant.DATE_FORMAT_DATETYPE));
            warningInfo.setCreateTime(new Date());
            warningInfo.setUpdateTime(new Date());
            String content = "监测到" + monitorDataThreshold.getName() + "当前数值" + monitorValue + ",超过阈值" + standArdValue;
            warningInfo.setWarningContent(content);
            warningMapper.addWarningInfo(warningInfo);
            try {
                messageService.sendMessage(warningInfo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            log.error("发生阈值报警,{}", content);

        }

    }

    /**
     * 调用分页插件完成分页
     *
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
