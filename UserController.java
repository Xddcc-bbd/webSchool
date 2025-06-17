package com.example.mystudent.controller;

import com.example.mystudent.entity.User;
import com.example.mystudent.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserMapper userMapper;
    //login
    @RequestMapping("/login")
    public User login(@RequestBody User user){
        return userMapper.login(user);
    }
    // 添加获取所有用户的接口
    @GetMapping("/getall")
    public List<User> getAllUsers() {
        return userMapper.getAll();
    }
}
