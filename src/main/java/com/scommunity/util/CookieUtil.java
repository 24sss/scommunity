package com.scommunity.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/14 9:29
 */
public class CookieUtil {
    /**
     * 从cookie中取值
     * 通过request得到cookie
     * 通过name（key）知道要取什么值
     */

    public static String getValue(HttpServletRequest request,String name){
        if(request==null||name==null){
            throw new IllegalArgumentException("参数为空");
        }
        /**从request中取cookie
         * */
        Cookie[] cookies = request.getCookies();
        /**想找到某一个值，就要遍历这个数组
         * */
        if(cookies!=null){
            for (Cookie cookie:cookies
                 ) {
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
        

    }
}
