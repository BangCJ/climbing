package com.bang.ap.dp.utils;

import com.bang.ap.dp.cons.DPConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ValidateUtil {

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = DPConstant.REGEX_PHONE;
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isEmailLegal(String str) throws PatternSyntaxException {
        String regExp = DPConstant.REGEX_EMAIL;
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
