package com.ytf.springboot.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytf.springboot.demo.Utils.RedisUtil;
import com.ytf.springboot.demo.dao.UserMapper;
import com.ytf.springboot.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:TengFeiYang
 * @Description:
 * @Date:Created in 2018/7/7
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${redis_key.user_all}")
    private String userAllKey;

    private static final ObjectMapper MAPPER = new ObjectMapper();

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

        if (redisUtil.hasKey(userAllKey)){
            try {
                String redisList = redisUtil.getByKey(userAllKey);
                JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, User.class);
                return MAPPER.readValue(redisList, javaType);
            } catch (IOException e) {
                LOGGER.error("解析json出错",e);
            }
            return new ArrayList<User>();
        } else {
            List<User> userList = userMapper.queryAll();
            String result = "";
            try {
                result = MAPPER.writeValueAsString(userList);
            } catch (JsonProcessingException e) {
                LOGGER.error("解析json出错",e);
                throw new Exception("内部错误");
            }
            redisUtil.setByKey(userAllKey,result,50);
            return userList;
        }
    }

}
