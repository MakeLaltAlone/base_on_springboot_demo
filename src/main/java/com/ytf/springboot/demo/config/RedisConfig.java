package com.ytf.springboot.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Auther: yangtengfei
 * @Date: 2018/7/24 16:42
 * @Description:
 */
@Configuration
@PropertySource("classpath:application.yml")
public class RedisConfig {

    @Value("${spring.redis.database}")
    private String database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.pool.max-active}")
    private String maxActive;
    @Value("${spring.redis.pool.max-wait}")
    private String maxWait;
    @Value("${spring.redis.pool.max-idle}")
    private String maxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private String minIdle;
    @Value("${spring.redis.pool.maxWaitMillis}")
    private String maxWaitMillis;
    @Value("${spring.redis.timeout}")
    private String timeout;

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(Integer.valueOf(maxIdle));
        jedisPoolConfig.setMaxTotal(Integer.valueOf(maxActive));
        jedisPoolConfig.setMinIdle(Integer.valueOf(minIdle));
        jedisPoolConfig.setMaxWaitMillis(Integer.valueOf(maxWaitMillis));
        return new JedisPool(jedisPoolConfig,host,Integer.valueOf(port),Integer.valueOf(timeout),password,Integer.valueOf(database));
    }
}





























//----------------------------------------------------------------------------------------------------------------------
// 以下是RedisTemplate版
// ---------------------------------------------------------------------------------------------------------------------






//package com.ytf.springboot.demo.config;
//
//import com.ytf.springboot.demo.Utils.RedisUtil;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
///**
// * @Auther: yangtengfei
// * @Date: 2018/7/24 16:42
// * @Description:
// */
//@Configuration
//@PropertySource("classpath:application.yml")
//public class RedisConfig {
//
//    @Value("${spring.redis.database}")
//    private String database;
//    @Value("${spring.redis.host}")
//    private String host;
//    @Value("${spring.redis.port}")
//    private String port;
//    @Value("${spring.redis.password}")
//    private String password;
//    @Value("${spring.redis.pool.max-active}")
//    private String maxActive;
//    @Value("${spring.redis.pool.max-wait}")
//    private String maxWait;
//    @Value("${spring.redis.pool.max-idle}")
//    private String maxIdle;
//    @Value("${spring.redis.pool.min-idle}")
//    private String minIdle;
//    @Value("${spring.redis.timeout}")
//    private String timeout;
//
//    /**
//     * JedisPoolConfig 连接池
//     * @return
//     */
//    @Bean
//    public JedisPoolConfig jedisPoolConfig() {
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        // 最大空闲数
//        jedisPoolConfig.setMaxIdle(Integer.valueOf(maxIdle));
//        // 连接池的最大数据库连接数
//        jedisPoolConfig.setMaxTotal(Integer.valueOf(maxActive));
//        // 最大建立连接等待时间
//        jedisPoolConfig.setMaxWaitMillis(Integer.valueOf(timeout));
//        return jedisPoolConfig;
//    }
//
//    /**
//     * 单机版配置
//     * @Title: JedisConnectionFactory
//     * @param @param jedisPoolConfig
//     * @param @return
//     * @return JedisConnectionFactory
//     * @autor lpl
//     * @date 2018年2月24日
//     * @throws
//     */
//    @Bean
//    public JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
//        JedisConnectionFactory JedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
//        //连接池
//        JedisConnectionFactory.setPoolConfig(jedisPoolConfig);
//        //IP地址
//        JedisConnectionFactory.setHostName(host);
//        //端口号
//        JedisConnectionFactory.setPort(Integer.valueOf(port));
//        //如果Redis设置有密码
//        //JedisConnectionFactory.setPassword(password);
//        //客户端超时时间单位是毫秒
//        JedisConnectionFactory.setTimeout(Integer.valueOf(timeout));
//        return JedisConnectionFactory;
//    }
//
//    /**
//     * 实例化 RedisTemplate 对象
//     *
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
//        return redisTemplate;
//    }
//    /**
//     * 设置数据存入 redis 的序列化方式,并开启事务
//     *
//     * @param redisTemplate
//     * @param factory
//     */
//    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
//        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        // 开启事务
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.setConnectionFactory(factory);
//    }
//    /**
//     * 注入封装RedisTemplate
//     * @Title: redisUtil
//     * @return RedisUtil
//     * @autor lpl
//     * @date 2017年12月21日
//     * @throws
//     */
//    @Bean(name = "redisUtil")
//    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
//        RedisUtil redisUtil = new RedisUtil();
//        redisUtil.setRedisTemplate(redisTemplate);
//        return redisUtil;
//    }
//}
