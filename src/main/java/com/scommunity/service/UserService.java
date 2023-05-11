package com.scommunity.service;

import com.scommunity.dao.LoginTicketMapper;
import com.scommunity.dao.UserMapper;
import com.scommunity.entity.LoginTicket;
import com.scommunity.entity.User;
import com.scommunity.util.CommunityConstant;
import com.scommunity.util.CommunityUtil;
import com.scommunity.util.MailClient;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.*;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 12:57
 */
@Service
public class UserService implements CommunityConstant {
    @Resource
    private UserMapper userMapper;

    //登录的时候一定会用到这个
    @Resource
    private LoginTicketMapper loginTicketMapper;

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
        System.out.println(url);
        System.out.println(domain);
        System.out.println(contextPath);
        System.out.println(user.getId());
        System.out.println(user.getActivationCode());
        context.setVariable("url",url);
        //利用模板引擎生成邮件的内容
        String content = templateEngine.process("/mail/activation",context);
        //发送邮件
        mailClient.sendMail(user.getEmail(),"激活账号",content);

        return map;
    }


    /**激活
     * 返回值：激活状态
     * 接收的参数：用户id，激活码
     *
     * */
    public int activation(int userId,String code){
         User user = userMapper.selectById(userId);
         //初始的时候他为0，激活之后他为1，如果他==1证明已经被激活了
         if(user.getStatus()==1){
             return ACTIVATION_REPEAT;

         }//用户传过来的与数据库中的一样，可以激活成功
         else if(user.getActivationCode().equals(code)){
             //激活成功的话就把用户内部的状态改为已激活状态
             userMapper.updateStatus(userId,1);
             return ACTIVATION_SUCCESS;
         }else {
             return ACTIVATION_FAILURE;
         }


    }

    /**
    登录的功能
     */
    /**
    password需要加密后才可以与数据库中的password比较，因为注册的时候数据库中的password是加密的
    登录的时候可能成功、可能失败、失败的原因还不只一种，所以用Map来存储登陆结果（可以封装多种情况的结果）
    expiredSeconds：过期时间（传入的是过期的秒数）
    * */
    public Map<String,Object> login(String username,String password,int expiredSeconds){
        /**封装的是登陆后的结果，可能成功，可能失败，失败还有多种原因
        * */
        Map<String,Object> map = new HashMap<>();

        /**空值的处理
         * */
        if(StringUtils.isBlank(username)){
            /**map中封装不能登录的原因，比如这里就是账号不能为空
             * 把map传给controller层，controller通过Model把
             * map中的数据传给前端显示
             * */
            map.put("usernameMsg","账号不能为空");
            return  map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return  map;
        }

        /**验证账号
         * */
        /**在数据库里查有没有这个username，如果没有直接返回错误信息
         * 如果有，再把password查出来，判断是否相同
        * */
        User user = userMapper.selectByName(username);
        if(user==null){
            map.put("usernameMsg","该账号不存在");
            return map;
        }

        /**状态判断
         * 因为注册之后，要通过邮件激活。如果没有激活也是不能登陆的
         * 激活的意思：注册之后，后端会随机生成一个字符串，拼接上路径发送给邮箱，当用户
         * 登录邮箱接收到这个路径的时候，点击这个链接，后端接收这个链接，判断
         * 传过来的随机字符串是否等于发给邮箱的字符串
         * 相等则激活成功，状态置为1，
         * 不相等则激活失败，状态还是默认值0
         *
         * */
        if(user.getStatus()==0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        //验证密码
        //注册的时候数据库存的是加密后的密码
        password=CommunityUtil.md5(password+user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg","密码不正确");
                return map;
            }

        //登录成功
        //生成登陆凭证，服务端记录下来，并传给客户端一个登陆凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        //有效状态
        loginTicket.setStatus(0);
        //设置过期时间
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));

        /**登录成功，登录凭证已经生成
         * 返回时需要把凭证放进去，我们要把凭证发给客户端
         * 可以把整个loginTicket全部发给客户端
         * 也可以只把Ticket发给客户端，页面只需要记这个内容
         * 浏览器只需要一个key，这是登录的凭证
         * 下次访问服务器的的时候，把这个key给服务器，服务器去库里找
         * 找到一条登录的数据，去看status（状态）和过期时间，还能看user_id（谁在登录）
         * 如果status==0的状态 也没有过期，就是登录成功
         * 作用相当于session
        * */
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    /**退出登录
     * 把凭证给我，证明是谁在退出
    * */
    public void logout(String ticket){
        //通过这个凭证，把状态改为1 （无效）
     loginTicketMapper.updateStatus(ticket,1);
    }


}
