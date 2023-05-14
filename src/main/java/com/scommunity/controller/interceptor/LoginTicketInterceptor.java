package com.scommunity.controller.interceptor;

import com.scommunity.dao.UserMapper;
import com.scommunity.entity.LoginTicket;
import com.scommunity.entity.User;
import com.scommunity.service.UserService;
import com.scommunity.util.CookieUtil;
import com.scommunity.util.HstHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/14 9:19
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    /**工具类，Threadlocal，存取数据
     * 一个请求对应一个线程，这里面存入的数据，在整个请求（线程）都是可以用的
     * 服务器做出响应，代表这个请求结束，代表这个线程被销毁，同时这里面存的数据也被销毁
     * */
    @Autowired
    private HstHolder hstHolder;

    //在请求的一开始就去获得这个ticket，利用ticket来查找user
    //同时把user暂存起来，在本次请求中持有用户
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**cookie通过request传过来的
         * 从request中取cookie ： 封装一下，好复用
         * 通过cookie得到ticket
         * */
        String ticket = CookieUtil.getValue(request,"ticket");




        /**如果ticket！=null 证明已经登录了
         * */
        if(ticket!=null){

            /**查询登录凭证
             * */
            System.out.println(ticket);
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            System.out.println(loginTicket);

            /**这个凭证有状态的，还要判断这个凭证有没有效
             * 检查凭证是否有效
             *     有效的话才能暂存，后面才能用
             *     无效的话，就相当于无效
             * */
            //不等于空，有效状态，过期时间晚于当前时间
            if(loginTicket!=null&&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                /**有效，就可以利用这个登录凭证查询user
                 * */
                User user = userService.findUserById(loginTicket.getUserId());
                /**让本次请求持有user
                 * 一个服务器处理多个请求，是并发的，每个请求来访问服务器 服务器都会创建一个独立的线程来处理这个请求
                 * 服务器处理请求是多线程的环境,我们想让每个请求都能持有User(同时是线程隔离的，每个线程都有user)
                 * */
                /**我们要想把一个数据存在一个地方，让并发访问都没有问题
                 * 要考虑线程的隔离，每个线程单独存一份，多个线程之间隔离
                 * 存这个对象，互相之间不干扰
                 * 工具类：HstHolder
                 * 用这个工具持有用户
                 *
                 * */
                /**把这个数据存到了当前线程对应的Map里
                 * 这个请求没有处理完，这个线程就一直还在，ThreadLocal里面的数据是一直都在的
                 * 当请求处理完，服务器向浏览器做出响应之后，这个线程被销毁
                 * */
                System.out.println(user);
                //作用：在本次请求中持有用户
                hstHolder.setUser(user);

            }

        }
        return true;
    }

    /**模板引擎之前就要用这个对象
     * 所以模板引擎之前我们就把这个对象存到model中，就可以用了
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /**先得到当前线程持有的user
         * 在模板引擎上就可以通过loginUser得到这个用户的数据
         * */
        User user = hstHolder.getUser();
        if(user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }


    }

    /**整个请求结束的时候清除这个user
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hstHolder.clear();
    }
}
