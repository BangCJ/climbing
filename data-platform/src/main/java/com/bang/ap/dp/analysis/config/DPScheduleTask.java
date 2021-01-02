package com.bang.ap.dp.analysis.config;

import com.bang.ap.dp.analysis.service.DataPesistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;


@Configuration
@EnableScheduling
@Slf4j
public class DPScheduleTask {
    @Autowired
    private DataPesistenceService dataPesistenceService ;

    @Scheduled(cron="0 15 0 ? * *")
    public void checkInfoAboutRoom() {
        log.info("data  platform  start to  do job ： checkInfoAboutRoom() ");
        try {
            //do things about frequence
            dataPesistenceService.saveFrequenceInRoom(new Date());
            //do things about used time length info
            dataPesistenceService.saveRoomUseTimeLength(new Date());
            //do things about stranger
            dataPesistenceService.saveStrangerInfo(new Date());

       } catch (Exception e) {
            log.error(" error occured when data platform  tried to  do job ");
            log.error(e.getMessage(), e);
        }

    }
}
