package com.ytf.springboot.demo.Utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;

/*
*@Auther: yangtengfei
* @Date: 2018/7/24 16:29
* @Description:  用的时候需要在需要使用的类型注入RedisUtil
*/
@Component
public class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private JedisPool jedisPool;


    /**
     * 因为每次利用Jedis操作Redis都要获取Jedis,所以这里利用了jdk1.8的新特性：函数式接口
     * @param fun
     * @param <T>
     * @return
     */
    public <T> T excute(Function<Jedis, T> fun){
        if(fun==null){
            return null;
        }
        Jedis jedis = null;
        try {
            // 从连接池中获取到jedis
            jedis = jedisPool.getResource();

            T apply = fun.apply(jedis);
            return apply;

        } catch (Exception e) {
            LOGGER.error("redis error");
        } finally {
            if (null != jedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                jedis.close();
            }
        }
        return null;
    }

    //=========================公共============================

    /**
     * 指定缓存的过期时间
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, int time){
        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.expire(key,time);
        };
        Long result = (Long) excute(function);

        if(result!=null&&result.equals(1)){
            return true;
        } else {
            return false;
        }
    }

    public boolean expire(byte[] key, int time){
        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.expire(key,time);
        };
        Long result = (Long) excute(function);

        if(result!=null&&result.equals(1)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取Key的过期时间
     * @param key
     * @return
     */
    public Long getExpire(String key){
        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.ttl(key);
        };
        Long result = (Long)excute(function);
        return result;
    }

    /**
     * 判断Key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.exists(key);
        };
        boolean result = (boolean)excute(function);
        return result;
    }

    /**
     * 删除key
     * @param key
     * @return
     */
    public long deleteKey(String key){
        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.del(key);
        };
        long result = (long)excute(function);
        return result;
    }


    //=========================String============================

    /**
     * 得到key中存储的内容
     *
     * @param key
     * @return
     */
    public String getByKey(String key) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("empty key, key {}", key);
            return null;
        }
        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.get(key);
        };
        Object result = excute(function);
        return result == null ? null : result.toString();
    }


    /**
     * 设置redis的key为value，超时为expire(不设置超时，传null)
     *
     * @param key
     * @return
     */
    public boolean setByKey(String key, String value, Integer expire) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            LOGGER.error("empty key or value, key {}, value {}", key, value);
            return false;
        }
        Function<Jedis, Object> function;

        if(expire == null){
            function = (Jedis jedis) -> {
                return jedis.set(key,value);
            };
        } else {
            function = (Jedis jedis) -> {
                return jedis.setex(key,expire,value);
            };
        }

        String result = (String) excute(function);
        return !(StringUtils.isBlank(result) || !result.equals("OK"));
    }

    /**
     * 设置redis的key为value，不存在时设置
     *
     * @param key
     * @return
     */
    public boolean setNxByKey(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            LOGGER.error("empty key or value, key {}, value {}", key, value);
            return false;
        }

        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.set(key,value,"nx");
        };
        Object excute = excute(function);
        if(excute == null || StringUtils.isBlank(excute.toString())){
            return false;
        }else if(excute.toString().equals("OK")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 设置redis的key为value，不存在时设置,支持超时
     *
     * @param key
     * @return
     */
    public boolean setNxByKey(String key, String value, Integer expire) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value) || expire == null) {
            LOGGER.error("empty key or value, key {}, value {}, expire {}", key, value, expire);
            return false;
        }

        Function<Jedis, Object> function = (Jedis jedis) -> {
            return jedis.set(key,value,"nx");
        };
        Object excute = excute(function);
        if(excute == null || StringUtils.isBlank(excute.toString())){
            return false;
        }else if(excute.toString().equals("OK")){
            return true;
        }else{
            return false;
        }
    }

    //==================================List================================

    /**
     * 放入list
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public Long lPushAll(String key, List value, Integer expire) {
        if (StringUtils.isBlank(key) || value ==null || !(value.size()>0)) {
            LOGGER.error("empty key or value, key {}, value {}", key, value);
            return null;
        }

        Function<Jedis, Object> function = (Jedis jedis) -> {
            final byte[] rawKey = rawKey(key);
            final byte[][] rawValues = rawValues(value);
            return jedis.lpush(rawKey,rawValues);
        };
        Object excute = excute(function);
        if(expire!=null&&expire>0){
            expire(rawKey(key),expire);
        }
        return (Long) excute;

    }

    /**
     * 取出List
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List lRange(String key,long start, long end) {
        if (StringUtils.isBlank(key)) {
            LOGGER.error("empty key or value, key {}, value {}");
            return null;
        }

        Function<Jedis, Object> function = (Jedis jedis) -> {
            final byte[] rawKey = rawKey(key);
            List<byte[]> bytes = jedis.lrange(rawKey, start, end);
            return deserializeValues(bytes);
        };
        Object excute = excute(function);

        return (List) excute;

    }

    //=========================================序列化/反序列化  （实体要实现Ser）=========================================================

    byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        if (key instanceof byte[]) {
            return (byte[]) key;
        }
        return serialize(key);
    }

    byte[][] rawValues(Collection<Object> values) {

        Assert.notEmpty(values, "Values must not be 'null' or empty.");
        Assert.noNullElements(values.toArray(), "Values must not contain 'null' value.");

        byte[][] rawValues = new byte[values.size()][];
        int i = 0;
        for (Object value : values) {
            rawValues[i++] = rawValue(value);
        }

        return rawValues;
    }

    byte[] rawValue(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        return serialize(value);
    }

    List<Object> deserializeValues(List<byte[]> rawValues) {
        // connection in pipeline/multi mode
        if (rawValues == null) {
            return null;
        }

        List<Object> values = new ArrayList<Object>(rawValues.size());

        for (byte[] bs : rawValues) {
            values.add(unserizlize(bs));
        }

        return values;
    }

    private static byte[] serialize(Object object) {
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byte[] getByte = byteArrayOutputStream.toByteArray();
            return getByte;
        } catch (Exception e) {
            LOGGER.error("序列化异常",e);
        } finally {
            try {
                if (objectOutputStream != null){
                    objectOutputStream.close();
                }
                if(byteArrayOutputStream != null){
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("序列化异常",e);
            }

        }
        return null;
    }

    private static Object unserizlize(byte[] binaryByte) {
        ObjectInputStream objectInputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        byteArrayInputStream = new ByteArrayInputStream(binaryByte);
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            return obj;
        } catch (Exception e) {
            LOGGER.error("序列化异常",e);
        } finally {
            try {
                if (objectInputStream != null){
                    objectInputStream.close();
                }
                if(byteArrayInputStream != null){
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("序列化异常",e);
            }

        }
        return null;
    }
}

























//----------------------------------------------------------------------------------------------------------------------
// 以下是RedisTemplate版
// ---------------------------------------------------------------------------------------------------------------------


//package com.ytf.springboot.demo.Utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * @Auther: yangtengfei
// * @Date: 2018/7/24 16:29
// * @Description:
// */
//public class RedisUtil {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);
//
//    private static RedisTemplate<String, Object> redisTemplate;
//
//    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    //=============================common============================
//    /**
//     * 指定缓存失效时间
//     * @param key 键
//     * @param time 时间(秒)
//     * @return
//     */
//    public boolean expire(String key,long time){
//        try {
//            if(time>0){
//                redisTemplate.expire(key, time, TimeUnit.SECONDS);
//            }
//            return true;
//        } catch (Exception e) {
//            LOGGER.error("RedisUtil.expire error",e);
//            return false;
//        }
//    }
//
//    /**
//     * 根据key 获取过期时间
//     * @param key 键 不能为null
//     * @return 时间(秒) 返回0代表为永久有效
//     */
//    public long getExpire(String key){
//        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
//    }
//
//    /**
//     * 判断key是否存在
//     * @param key 键
//     * @return true 存在 false不存在
//     */
//    public boolean hasKey(String key){
//        try {
//            return redisTemplate.hasKey(key);
//        } catch (Exception e) {
//            LOGGER.error("RedisUtil.hasKey error",e);
//            return false;
//        }
//    }
//
//    /**
//     * 删除缓存
//     * @param key 可以传一个值 或多个
//     */
//    @SuppressWarnings("unchecked")
//    public void del(String ... key){
//        if(key!=null&&key.length>0){
//            if(key.length==1){
//                redisTemplate.delete(key[0]);
//            }else{
//                redisTemplate.delete(CollectionUtils.arrayToList(key));
//            }
//        }
//    }
//
//    //============================String=============================
//    /**
//     * 普通缓存获取
//     * @param key 键
//     * @return 值
//     */
//    public Object get(String key){
//        return key==null?null:redisTemplate.opsForValue().get(key);
//    }
//
//    /**
//     * 普通缓存放入
//     * @param key 键
//     * @param value 值
//     * @return true成功 false失败
//     */
//    public static boolean set(String key,Object value) {
//        try {
//            redisTemplate.opsForValue().set(key, value);
//            return true;
//        } catch (Exception e) {
//            LOGGER.error("RedisUtil.set error",e);
//            return false;
//        }
//
//    }
//
//    /**
//     * 普通缓存放入并设置时间
//     * @param key 键
//     * @param value 值
//     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
//     * @return true成功 false 失败
//     */
//    public boolean set(String key,Object value,long time){
//        try {
//            if(time>0){
//                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
//            }else{
//                set(key, value);
//            }
//            return true;
//        } catch (Exception e) {
//            LOGGER.error("RedisUtil.set error",e);
//            return false;
//        }
//    }
//
//    /**
//     * 递增
//     * @param key 键
//     * @param by 要增加几(大于0)
//     * @return
//     */
//    public long incr(String key, long delta){
//        if(delta<0){
//            throw new RuntimeException("递增因子必须大于0");
//        }
//        return redisTemplate.opsForValue().increment(key, delta);
//    }
//
//    /**
//     * 递减
//     * @param key 键
//     * @param by 要减少几(小于0)
//     * @return
//     */
//    public long decr(String key, long delta){
//        if(delta<0){
//            throw new RuntimeException("递减因子必须大于0");
//        }
//        return redisTemplate.opsForValue().increment(key, -delta);
//    }
//
//    //================================Map=================================
//    /**
//     * HashGet
//     * @param key 键 不能为null
//     * @param item 项 不能为null
//     * @return 值
//     */
//    public Object hget(String key,String item){
//        return redisTemplate.opsForHash().get(key, item);
//    }
//
//    /**
//     * 获取hashKey对应的所有键值
//     * @param key 键
//     * @return 对应的多个键值
//     */
//    public Map<Object,Object> hmget(String key){
//        return redisTemplate.opsForHash().entries(key);
//    }
//
//    /**
//     * HashSet
//     * @param key 键
//     * @param map 对应多个键值
//     * @return true 成功 false 失败
//     */
//    public boolean hmset(String key, Map<String,Object> map){
//        try {
//            redisTemplate.opsForHash().putAll(key, map);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * HashSet 并设置时间
//     * @param key 键
//     * @param map 对应多个键值
//     * @param time 时间(秒)
//     * @return true成功 false失败
//     */
//    public boolean hmset(String key, Map<String,Object> map, long time){
//        try {
//            redisTemplate.opsForHash().putAll(key, map);
//            if(time>0){
//                expire(key, time);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 向一张hash表中放入数据,如果不存在将创建
//     * @param key 键
//     * @param item 项
//     * @param value 值
//     * @return true 成功 false失败
//     */
//    public boolean hset(String key,String item,Object value) {
//        try {
//            redisTemplate.opsForHash().put(key, item, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 向一张hash表中放入数据,如果不存在将创建
//     * @param key 键
//     * @param item 项
//     * @param value 值
//     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
//     * @return true 成功 false失败
//     */
//    public boolean hset(String key,String item,Object value,long time) {
//        try {
//            redisTemplate.opsForHash().put(key, item, value);
//            if(time>0){
//                expire(key, time);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 删除hash表中的值
//     * @param key 键 不能为null
//     * @param item 项 可以使多个 不能为null
//     */
//    public void hdel(String key, Object... item){
//        redisTemplate.opsForHash().delete(key,item);
//    }
//
//    /**
//     * 判断hash表中是否有该项的值
//     * @param key 键 不能为null
//     * @param item 项 不能为null
//     * @return true 存在 false不存在
//     */
//    public boolean hHasKey(String key, String item){
//        return redisTemplate.opsForHash().hasKey(key, item);
//    }
//
//    /**
//     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
//     * @param key 键
//     * @param item 项
//     * @param by 要增加几(大于0)
//     * @return
//     */
//    public double hincr(String key, String item,double by){
//        return redisTemplate.opsForHash().increment(key, item, by);
//    }
//
//    /**
//     * hash递减
//     * @param key 键
//     * @param item 项
//     * @param by 要减少记(小于0)
//     * @return
//     */
//    public double hdecr(String key, String item,double by){
//        return redisTemplate.opsForHash().increment(key, item,-by);
//    }
//
//    //============================set=============================
//    /**
//     * 根据key获取Set中的所有值
//     * @param key 键
//     * @return
//     */
//    public Set<Object> sGet(String key){
//        try {
//            return redisTemplate.opsForSet().members(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 根据value从一个set中查询,是否存在
//     * @param key 键
//     * @param value 值
//     * @return true 存在 false不存在
//     */
//    public boolean sHasKey(String key,Object value){
//        try {
//            return redisTemplate.opsForSet().isMember(key, value);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 将数据放入set缓存
//     * @param key 键
//     * @param values 值 可以是多个
//     * @return 成功个数
//     */
//    public long sSet(String key, Object...values) {
//        try {
//            return redisTemplate.opsForSet().add(key, values);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    /**
//     * 将set数据放入缓存
//     * @param key 键
//     * @param time 时间(秒)
//     * @param values 值 可以是多个
//     * @return 成功个数
//     */
//    public long sSetAndTime(String key,long time,Object...values) {
//        try {
//            Long count = redisTemplate.opsForSet().add(key, values);
//            if(time>0) expire(key, time);
//            return count;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    /**
//     * 获取set缓存的长度
//     * @param key 键
//     * @return
//     */
//    public long sGetSetSize(String key){
//        try {
//            return redisTemplate.opsForSet().size(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    /**
//     * 移除值为value的
//     * @param key 键
//     * @param values 值 可以是多个
//     * @return 移除的个数
//     */
//    public long setRemove(String key, Object ...values) {
//        try {
//            Long count = redisTemplate.opsForSet().remove(key, values);
//            return count;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//    //===============================list=================================
//
//    /**
//     * 获取list缓存的内容
//     * @param key 键
//     * @param start 开始
//     * @param end 结束  0 到 -1代表所有值
//     * @return
//     */
//    public List<Object> lGet(String key, long start, long end){
//        try {
//            return redisTemplate.opsForList().range(key, start, end);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 获取list缓存的长度
//     * @param key 键
//     * @return
//     */
//    public long lGetListSize(String key){
//        try {
//            return redisTemplate.opsForList().size(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    /**
//     * 通过索引 获取list中的值
//     * @param key 键
//     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
//     * @return
//     */
//    public Object lGetIndex(String key,long index){
//        try {
//            return redisTemplate.opsForList().index(key, index);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 将list放入缓存
//     * @param key 键
//     * @param value 值
//     * @param time 时间(秒)
//     * @return
//     */
//    public boolean lSet(String key, Object value) {
//        try {
//            redisTemplate.opsForList().rightPush(key, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 将list放入缓存
//     * @param key 键
//     * @param value 值
//     * @param time 时间(秒)
//     * @return
//     */
//    public boolean lSet(String key, Object value, long time) {
//        try {
//            redisTemplate.opsForList().rightPush(key, value);
//            if (time > 0) expire(key, time);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 将list放入缓存
//     * @param key 键
//     * @param value 值
//     * @param time 时间(秒)
//     * @return
//     */
//    public boolean lSet(String key, List<Object> value) {
//        try {
//            redisTemplate.opsForList().rightPushAll(key, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 将list放入缓存
//     * @param key 键
//     * @param value 值
//     * @param time 时间(秒)
//     * @return
//     */
//    public boolean lSet(String key, List<Object> value, long time) {
//        try {
//            redisTemplate.opsForList().rightPushAll(key, value);
//            if (time > 0) expire(key, time);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 根据索引修改list中的某条数据
//     * @param key 键
//     * @param index 索引
//     * @param value 值
//     * @return
//     */
//    public boolean lUpdateIndex(String key, long index,Object value) {
//        try {
//            redisTemplate.opsForList().set(key, index, value);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 移除N个值为value
//     * @param key 键
//     * @param count 移除多少个
//     * @param value 值
//     * @return 移除的个数
//     */
//    public long lRemove(String key,long count,Object value) {
//        try {
//            Long remove = redisTemplate.opsForList().remove(key, count, value);
//            return remove;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//}
