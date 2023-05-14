package com.scommunity.util;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/14 9:53
 */

import com.scommunity.entity.User;
import org.springframework.stereotype.Component;

/**持有用户的信息，用于代替session对象
 * 把用户信息存到ThreadLocal里，起到线程隔离的作用
 * */
@Component
public class HstHolder {
    //可以线程隔离
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
       return users.get();
    }

    /**请求结束的时候，清理
     * */
    public void clear(){
        users.remove();
    }



}
