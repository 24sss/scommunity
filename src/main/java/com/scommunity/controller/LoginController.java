package com.scommunity.controller;

import com.scommunity.entity.User;
import com.scommunity.service.UserService;
import com.scommunity.util.CommunityConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/8 12:57
 */
@Controller
public class LoginController implements CommunityConstant {
    @Resource
    private UserService userService;

    //前端点击首页中的注册按钮后，这个controller来处理，就进入register.html中填写数据
    @RequestMapping(path= "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        System.out.println("注册页面");
        return "/site/register";
    }
    //首页点击登录，或者激活成功跳转到这里，只是到登录页面，我们在登陆页面填写信息，点击登录，会发出另一个请求，到另一个controller
    @RequestMapping(path= "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        System.out.println("注册页面");
        return "/site/login";
    }


    //这个方法处理注册的请求
    //返回值是视图的名字
    //Model：保存后台传给前端的值
    //User：前端传给后台的值（注册信息）
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        System.out.println("zhuce......");
        System.out.println(user);
        //调用service层去注册，根据不同的注册结果返回一个map
        Map<String, Object> map = userService.register(user);
        //注册成功，跳到中间页面 最终跳到首页去
        if(map==null||map.isEmpty()){
            System.out.println("注册成功");
            //向前端传送数据
            model.addAttribute("msg","注册成功，我们已向您的邮箱发送了一条激活邮件，请尽快激活！");
            model.addAttribute("target","/index");
           //跳转到这个页面
            return "/site/operate-result";
        }else {
            //代表注册不成功
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            //注册失败就返回到注册页面
            return "/site/register";
        }

    }
    ////http://localhost:8080/sss/activation/101/code
    //Model用来向将要返回的模板传参
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int result = userService.activation(userId, code);
        if(result==ACTIVATION_SUCCESS){
            /**先跳转到operate-result，显示激活成功，最终跳转到登录页面
             * */
            model.addAttribute("msg","激活成功，您的帐号可以正常使用");
            model.addAttribute("target","/login");

        }else if(result==ACTIVATION_REPEAT){
            /**重复登陆
             * */
            model.addAttribute("msg","该账号已被激活");
            model.addAttribute("target","/index");


        }else {
            //激活失败
            model.addAttribute("msg","激活失败，您提供的激活码不正确");
            model.addAttribute("target","/index");

        }
        return "/site/operate-result";
    }



}
