package com.xcw.service;

import com.xcw.entity.User;
import com.xcw.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class: UserService
 * @author: ChengweiXing
 * @description: TODO
 **/
@Service
public class UserService {


    @Autowired
    private UserMapper userMapper;

    public User getById(int id){
        return userMapper.selectById(id);
    }
}
