package com.scommunity.util;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/8 10:34
 */

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
提供的是发送邮件的功能
* */
@Component
public class MailClient {
    /**
     * 声明一个日志，下面要记录日志
     */
    private static final Logger logger= LoggerFactory.getLogger(MailClient.class);

    //把JavaMailSender注入进来
    //JavaMailSender是Spring Email的核心组件，负责发送邮件。
    @Resource
    private JavaMailSender mailSender;

    //发件人
    @Value("${spring.mail.username}")
    private String from;

    //发送邮件的方法，供外部调用
    //参数：发送给谁，主题是什么，内容是什么
    public void sendMail(String to,String subject,String content){

        try{
            //创建MimeMessage，但是里面是空的
            //用于封装邮件的相关信息
            MimeMessage message = mailSender.createMimeMessage();

            //构建helper用来帮我们填入内容
            //MimeMessageHelper用于辅助构建MimeMessage对象
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            //发送
            mailSender.send(helper.getMimeMessage());
        }catch (MessagingException e){
            logger.error("发送邮件失败"+e.getMessage());
        }


    }




}
