package com.bang.ap.dp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.bang")
@MapperScan("com.bang.ap.dp.web.mapper")
public class DataPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataPlatformApplication.class, args);
	}

}
