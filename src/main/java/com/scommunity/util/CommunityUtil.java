package com.scommunity.util;

import org.junit.platform.commons.util.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/8 13:20
 */
//注册功能的工具类
public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密
    //hello->asdf
    //hello+123->asdfefrb
    public static String md5(String key){
        //null," "  ""都能判断出来
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
