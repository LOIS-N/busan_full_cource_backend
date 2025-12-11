package com.ssafy.gt.mapper;

import com.ssafy.gt.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {

    User regist(User user);

    User login(User user);

    User selectByemail(User user);

    User selectByid(User user);

    User update(User user);
}
