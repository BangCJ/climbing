package com.bang.ap.dp.analysis.config;

import com.bang.ap.dp.analysis.service.DataPesistenceService;
import com.bang.ap.dp.receive.hikvision.service.HikSensorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;


@Configuration
@EnableScheduling
@Slf4j
public class DPScheduleTask {


    @Value("${ap.sensor.save.schedule:5}")
    private int interval;

    @Autowired
    private DataPesistenceService dataPesistenceService;

    @Autowired
    private HikSensorService hikSensorService;

    @Scheduled(cron = "0 15 0 ? * *")
    public void checkDailyData() {
        log.error("data  platform  start to  do job ： checkDailyData() !!!!!!!!!!");
        try {
            //do things about frequence
            dataPesistenceService.saveFrequenceInRoom(new Date());
            //do things about used time length info
            dataPesistenceService.saveRoomUseTimeLength(new Date());
            //do things about stranger
            dataPesistenceService.saveStrangerInfo(new Date());
            //do things about importantPeopleInfo
            dataPesistenceService.saveImportantPeopleInfo(new Date());

        } catch (Exception e) {
            log.error(" error occured when data platform  tried to  do job checkInfoAboutRoom ");
            log.error(e.getMessage(), e);
        }

    }


    @Scheduled(fixedRate = 30 * 1000 * 1)
    public void saveHikSensorData() {
        log.info("data  platform  start to  do job ： saveHikSensorData() ");
        try {
            hikSensorService.saveSensorInfo();
            hikSensorService.saveFireSensor();
        } catch (Exception e) {
            log.error(" error occured when data platform  tried to  do job saveSensorInfo ");
            log.error(e.getMessage(), e);
        }

    }

}
