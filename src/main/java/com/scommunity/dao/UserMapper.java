package com.scommunity.dao;

import com.scommunity.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 13:04
 */
@Mapper
public interface UserMapper {

    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);
    int insertUser(User user);
    //根据id修改状态，后面激活账号的时候用
    int updateStatus(int id,int status);
    //更新头像
    int updateHeader(int id,String headerUrl);
    int updatePassword(int id,String password);

}
