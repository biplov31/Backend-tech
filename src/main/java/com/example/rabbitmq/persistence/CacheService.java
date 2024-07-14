package com.example.rabbitmq.persistence;

import com.example.rabbitmq.student.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    private final RedisTemplate<String, String> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public CacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Student get(String key) {
        try {
            String jsonStr = redisTemplate.opsForValue().get(key);
            if (jsonStr == null) return null;

            return objectMapper.readValue(jsonStr.toString(), Student.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(String key, Student student, Long ttl) {
        try {
            String jsonValue = objectMapper.writeValueAsString(student);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
            System.out.println("Student cached!");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
