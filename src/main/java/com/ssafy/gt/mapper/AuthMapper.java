package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    User login(User user);
}
