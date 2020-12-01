package com.bang.ap.dp;

import com.bang.ap.dp.web.entity.MonitorData;
import com.bang.ap.dp.web.service.MonitorDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class DataPlatformApplicationTests {
	@Autowired
	private MonitorDataService monitorDataService;

	@Test
	void contextLoads() {
	}

	void addMonitorData(MonitorData monitorData){
		try {
			monitorData.setCode("");
			monitorDataService.addMonitorData(monitorData);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}


}
