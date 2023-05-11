package com.scommunity.controller;

import com.google.code.kaptcha.Producer;
import com.scommunity.entity.User;
import com.scommunity.service.UserService;
import com.scommunity.util.CommunityConstant;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
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

    //整个项目的路径
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //创建日志对象
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    //生成验证码
    @Resource
    private Producer kaptchaProducer;

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

    /**
     首页点击登录的时候，发出一个get请求/login,
     这个请求会返回一个html页面(逻辑在上方)，这个页面包含了
     一张验证码链接，点击这个链接会发出新的请求，
     我们在这里写一个controller来处理这个请求
     也就是说在那个html页面中包含了这个链接
     */
    /**
     * 这里我们向浏览器输出的是一张图片，不是字符串，也不是html，所以
     * 返回值是void， 我们用response对象手动的去输出
     *
     */
    @RequestMapping(value = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        /**
         * 我们生成验证码之后，服务端要把它记住，
         * 我们登录的时候要对他进行验证，看验证码对不对
         * 验证码不能存在浏览器端，很容易被破解
         * 所以存在服务器端
         * 这次请求的时候生成验证码
         * 登录的时候是一次新的请求，所以是跨请求的
         * 所以我们存储在Session里面
         */
        //生成验证码
        String text = kaptchaProducer.createText();
        //利用验证码生成一个与之对应的图片
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        session.setAttribute("kaptcha",text);

        //将图片输出给浏览器
        response.setContentType("image/png");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);

        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }


    }


    /**
     * 接收表单提交给我的数据，所以请求方式是Post
     * username:用户名
     * password:密码
     * code:验证码
     * rememberme:是否勾选了记住我
     * model:返回给客户端的数据
     * session:生成的验证码在服务端保存在了session中，我们要判断用户输入的验证码code与之前生成的验证码是否一致
     * response:如果登陆成功，我们要把ticket发放给客户端来保存，用cookie来保存
     */
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String username,String password,String code,boolean rememberme,Model model,
                        HttpSession session,HttpServletResponse response){

        /**先判断验证码对不对
         * 是不是空
         * 忽略大小写：equalsIgnoreCase
        * */
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)){
           model.addAttribute("codeMsg","验证码不正确");
           return "/site/login";
        }

        /**检查账号、密码
         * 还有一个参数是过期时间
         * 我们定义两个常量，代表”记住我“和“不记住我”各自的超时时间
         * 用哪个值呢，我们得看用户有没有勾选记住我
        * */
        /**过期时间：我们根据用户有没有勾选记住我，有不同的过期时间
         * */
        int expiredSeconds=rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        //如果map里有ticket证明登陆成功
        if(map.containsKey("ticket")){
            //登录成功的话，向客户端发送一个携带ticket（登陆凭证）的cookie
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            //登录成功后，生效范围应该是整个项目；
            cookie.setPath(contextPath);
            //设置cookie的有效时间
            cookie.setMaxAge(expiredSeconds);
            //把cookie添加至response，在响应的时候就发送给客户端了
            response.addCookie(cookie);
            return "redirect:/index";
        }else{
            //登录失败
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }


    }

    /**退出的请求
     * */
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }


}
