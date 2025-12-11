package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    Integer regist(User user);

    User login(User user);

    User checkUserId(User user);

    User checkEmail(User user);

    User selectByUserId(User user);

    int update(User user);
}
