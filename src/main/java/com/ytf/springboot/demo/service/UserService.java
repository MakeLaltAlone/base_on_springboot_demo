package com.ytf.springboot.demo.service;

import com.ytf.springboot.demo.dao.UserMapper;
import com.ytf.springboot.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/7
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 新增
     * @param user
     */
    public void addUser(User user) throws Exception{
        userMapper.insertSelective(user);
    }

    /**
     * 查询所有
     * @return
     * @throws Exception
     */
    public List<User> queryUserAll() throws Exception{
        return userMapper.queryAll();
    }

}
