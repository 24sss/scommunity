package com.scommunity;

import com.scommunity.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/8 11:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ScommunityApplication.class)
public class MailTests {
    //发送邮件的类
    @Resource
    private MailClient mailClient;

    //利用模板引擎发送
    //TemplateEngine是模板引擎，负责格式化HTML格式的邮件
    @Resource
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){
        mailClient.sendMail("17853071995@163.com","test","sss");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","sun");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);

        mailClient.sendMail("17853071995@163.com","html",content);
    }

}
