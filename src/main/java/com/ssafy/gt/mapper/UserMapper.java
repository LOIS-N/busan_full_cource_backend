package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.User;

public interface UserMapper {

    int regist(User user);

    User checkUserId(User user);

    User checkEmail(User user);

    User selectByUserId(String userId);

    int update(User user);

    int updateUserPassword(User user);
}
