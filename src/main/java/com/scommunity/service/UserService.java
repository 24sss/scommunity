package com.scommunity.service;

import com.scommunity.dao.UserMapper;
import com.scommunity.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 12:57
 */
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public User findUserById(int id){
       return userMapper.selectById(id);
    }

}
