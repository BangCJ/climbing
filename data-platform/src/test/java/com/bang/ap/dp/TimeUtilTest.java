package com.bang.ap.dp;

import com.bang.ap.dp.cons.DPConstant;
import com.bang.ap.dp.utils.DPTimeUtil;

public class TimeUtilTest {
    public static void main(String[] args) {
        try {
           /* String returnTime=DPTimeUtil.isoStr2utc8Str("2017-06-14T00:00:00.000+08:00", DPConstant.DATE_FORMAT);
            System.out.println("iso -> normal: "+returnTime);
            String returnIso=DPTimeUtil.utc8Str2IsoStr(returnTime,DPConstant.DATE_FORMAT);
            System.out.println("normal -> iso"+ returnIso);

            String returnTime2=DPTimeUtil.isoStr2utc8Str("2017-06-14T00:00:00.000+0800", DPConstant.DATE_FORMAT);
            System.out.println("iso -> normal: "+returnTime2);*/

            String time = DPTimeUtil.utc8Str2IsoStr(DPTimeUtil.formatDate(DPTimeUtil.getTomorrow()), DPConstant.DATE_FORMAT);
            System.out.println(time);







        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
