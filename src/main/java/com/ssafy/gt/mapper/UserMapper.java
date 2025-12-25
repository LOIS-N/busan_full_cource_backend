package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.User;

public interface UserMapper {

    int regist(User user);

    User checkUserId(String userId);

    User checkEmail(String email);

    User selectById(Integer id);

    int update(User user);

    int updateUserPassword(User user);
}
