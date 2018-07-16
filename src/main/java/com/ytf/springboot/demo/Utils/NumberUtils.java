package com.ytf.springboot.demo.Utils;

import java.lang.reflect.Parameter;
import java.util.regex.Pattern;

/**
 * @Author:TengFeiYang
 * @Description: 关于数字的工具类
 * @Date:Created in 2018/7/7
 */
public class NumberUtils {

    /**
     * 判断非负数的整数或者携带一位或者两位的小数
     * @param obj
     * @return
     */
    public static boolean judgeTwoDecimal(Object obj){
        boolean flag = false;
        try {
            if(obj != null){
                String source = obj.toString();
                // 判断是否是整数或者是携带一位或者两位的小数
                Pattern pattern = Pattern.compile("^[+]?([0-9]+(.[0-9]{1,2})?)$");
                if(pattern.matcher(source).matches()){
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
