package com.scommunity.dao;

import com.scommunity.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 10:40
 */
//DiscussPost帖子实体类的Mapper
@Mapper
public interface DiscussPostMapper {
    /**
     查询帖子，社区首页不需要根据用户id查询，是全部的数据，但是我们后面会有用户主页的帖子，这里就用来userid
    这里使用动态sql，userid==0的时候，不拼入userid，不为0的时候 拼入userid
    分页查询：offset,起始行的行号，每一页最多多少行数据
    * */
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    /**
     * 一共多少条帖子
     * 这里也是动态sql，用户主页才会用到它
     * @Param:起别名，动态sql中要用到这个参数的时候，并且方法只有这一个参数，一定要起别名
    * */
    int selectDiscussPostRows(@Param("userId") int userId);

}
