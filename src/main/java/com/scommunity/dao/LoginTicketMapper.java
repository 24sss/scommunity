package com.scommunity.dao;

import com.scommunity.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/10 14:31
 */
@Mapper
public interface LoginTicketMapper {
    //登录成功之后要插入一个凭证
    //自动生成主键，并把这个主键注入给id
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    //查询
    //ticket发给客户端，客户端利用cookie（存储了ticket）访问服务器的时候
    //把ticket给服务器，我们就利用ticket查到整条数据。我们就知道是哪个用户在登陆访问
    //ticket是一个唯一的标识，不能重复
    @Select({
            "select id,user_id,ticket,status,expired from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    //修改凭证的状态，退出的时候，凭证失效，要么删除，要么更改状态，我们这里采用更改状态
    @Update({
            "update login_ticket set status=#{status}  where ticket=#{ticket}"
    })
    int updateStatus(String ticket,int status);



}

