package com.example.mystudent.mapper;

import com.example.mystudent.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from users where userName=#{userName} and userPwd=#{userPwd}")
    User login(User user);
    @Select("select * from users")
    List<User> getAll();
}
