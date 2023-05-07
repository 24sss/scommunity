package com.scommunity;

import com.scommunity.dao.DiscussPostMapper;
import com.scommunity.dao.UserMapper;
import com.scommunity.entity.DiscussPost;
import com.scommunity.entity.User;
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

        User user1 = userMapper.selectByName("liubei");
        System.out.println(user1);

        User user2  = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user2);

    }
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("测");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser(){
        int rows = userMapper.updateStatus(150,1);
        System.out.println(rows);

        rows =userMapper.updatePassword(150,"1234");
    }

}
