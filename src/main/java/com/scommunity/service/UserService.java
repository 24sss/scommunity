package com.scommunity.service;

import com.scommunity.dao.UserMapper;
import com.scommunity.entity.User;
import com.scommunity.util.CommunityUtil;
import com.scommunity.util.MailClient;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 12:57
 */
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    //因为要发邮件所以要注入客户端
    @Resource
    private MailClient mailClient;

    //注入模板引擎
    @Resource
    private TemplateEngine templateEngine;

    //发邮件的时候要生成一个激活码，激活码中包含的域名
    @Value("${community.path.domain}")
    private String domain;

    //激活码中包含的项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){
       return userMapper.selectById(id);
    }

    //写一个共有的方法，便于别人复用
    /**
     * 返回信息：账号密码不能为空，账号已存在等信息
     * 参数：注册的时候 要把账号密码邮箱等信息传给我，所以参数是User
     * */
    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        //空值处理
        if(user==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //账号是空的
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        //密码是空的
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //邮箱是空的
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }

        //验证账号是否存在
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMsg","该账号已被注册");
            return map;
        }
        //验证邮箱是否已经存在
        u=userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMsg","该邮箱已被注册");
            return map;
        }

        /**
         * 注册用户
        * */
        //给user的salt属性一个五位的随机的字符串
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        //将原来的密码加上salt，进行md5加密，加密后的值存入password中
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        //默认普通用户
        user.setType(0);
        //默认没有激活
        user.setStatus(0);
        //激活码
        user.setActivationCode(CommunityUtil.generateUUID());
        //给他一个随机的头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        //注册的时间
        user.setCreateTime(new Date());

        //最后添加到数据库里
        userMapper.insertUser(user);

        //发送激活邮件
        //这个对象用来携带变量
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //url路径
        //希望服务器接收到的路径：
        //http://localhost:8080/sss/activation/101/code
        String url = domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        //利用模板引擎生成邮件的内容
        String content = templateEngine.process("/mail/activation",context);
        //发送邮件
        mailClient.sendMail(user.getEmail(),"激活账号",content);

        return map;
    }


}
