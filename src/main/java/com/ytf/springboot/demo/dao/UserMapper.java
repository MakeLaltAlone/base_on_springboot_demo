package com.ytf.springboot.demo.dao;

import com.ytf.springboot.demo.model.User;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);
}