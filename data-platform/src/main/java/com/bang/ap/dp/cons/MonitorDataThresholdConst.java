package com.bang.ap.dp.cons;


import java.util.HashMap;
import java.util.Map;

public class MonitorDataThresholdConst {
	public static Map<String,String>threshold=new HashMap<>();
	public static Map<String,String>thresholdName=new HashMap<>();
	static{

		thresholdName.put("gas","CO浓度");
		thresholdName.put("humidity","大气压力");
		thresholdName.put("temp","温度");
		thresholdName.put("wind","风力");
		thresholdName.put("wave","震动");
		thresholdName.put("voicePress","声压");

		threshold.put("gas","0.1");
		threshold.put("humidity","30");
		threshold.put("temp","40");
		threshold.put("wind","40");
		threshold.put("wave","0.5");
		threshold.put("voicePress","0.5");

	}




}
