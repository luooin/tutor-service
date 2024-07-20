package com.example.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

//写静态方法不需要加入容器
public class CommunityUtil {

    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }


}
