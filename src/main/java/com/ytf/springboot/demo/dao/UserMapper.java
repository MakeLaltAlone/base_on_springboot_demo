package com.ytf.springboot.demo.dao;

import com.ytf.springboot.demo.model.User;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    List<User> queryAll();
}