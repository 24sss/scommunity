package com.scommunity;

import com.scommunity.dao.DiscussPostMapper;
import com.scommunity.dao.LoginTicketMapper;
import com.scommunity.dao.UserMapper;
import com.scommunity.entity.DiscussPost;
import com.scommunity.entity.LoginTicket;
import com.scommunity.entity.User;
import com.scommunity.util.CommunityConstant;
import com.scommunity.util.CommunityUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 11:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ScommunityApplication.class)
public class MapperTests {

    @Resource
    LoginTicketMapper loginTicketMapper;
    @Resource
    DiscussPostMapper discussPostMapper;

    @Resource
    UserMapper userMapper;

    @Test
    public void testSelectPost(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0,0,10);
        for (DiscussPost post:
             list) {
            System.out.println(post);
        }
        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);

    }

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        User user1 = userMapper.selectByName("a6");
        System.out.println(user1);

        User user2  = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user2);

    }
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("a5");
        user.setPassword("a12345");
        user.setSalt("abc");
        user.setStatus(1);
        user.setEmail("test@qq.com");
       // user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){

      //int rows =
              userMapper.updateStatus(163,1);
       // System.out.println(rows);

       // rows =
     /*   String s = CommunityUtil.md5("a12345");
                userMapper.updatePassword(162,s);*/
    }


    @Test
    public void loginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));

        loginTicketMapper.insertLoginTicket(loginTicket);

    }

    @Test
    public void testselectloginticket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc",0);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }


    @Test
    public void testgettaticket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("3d822c0d68884456bbe79669f8935820");
        System.out.println(loginTicket);
    }

}
