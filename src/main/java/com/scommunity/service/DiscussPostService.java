package com.scommunity.service;

import com.scommunity.dao.DiscussPostMapper;
import com.scommunity.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 12:49
 */
@Service
public class DiscussPostService {
    @Resource
    private DiscussPostMapper discussPostMapper;

    /**
     * userId,我们在页面上显示的时候，肯定不能显示userId，要显示用户名
     * 方法一：写sql的时候，利用外键userId关联查询用户姓名
     * 方法二：得到DiscussPost之后，对每一个DiscussPost单独查询user名称，然后把用户名和DiscussPost组合在一起返回
    * */
    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
