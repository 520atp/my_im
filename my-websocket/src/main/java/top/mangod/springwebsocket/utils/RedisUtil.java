package top.mangod.springwebsocket.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;


    public void set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key,String value,long time){
        redisTemplate.opsForValue().set(key,value,time);
    }

    public void del(String key){
        redisTemplate.delete(key);
    }


}
