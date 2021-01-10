package com.bang.ap.dp.cons;


import java.util.HashMap;
import java.util.Map;

public class WarningTypeConst {


    public static Map<String, String> warningNameMap = new HashMap<>();


    static {
        warningNameMap.put("intrusionWarning", "入侵预警");
        warningNameMap.put("sensorWarning", "传感器预警");
        warningNameMap.put("trendWarning", "趋势预警");
        warningNameMap.put("otherWarning", "其他预警");
    }


}
