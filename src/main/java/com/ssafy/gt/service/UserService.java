package com.ssafy.gt.service;

import com.ssafy.gt.dto.User;
import com.ssafy.gt.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public User regist(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userMapper.regist(user);

        user.setPassword(null);

        return user;
    }

    /**
     * 회원정보 조회
     */
    public User selectByUserId(User user) {
        return userMapper.selectByUserId(user);
    }

    /**
     * 아이디 중복 검사
     */
    public Integer checkUserId(User user) {
        User Checkuser = userMapper.checkUserId(user);
        if(Checkuser != null) { return 1; }
        return 0;
    }

    /**
     * 이메일 중복 검사
     */
    public Integer checkEmail(User user) {
        User Checkuser = userMapper.checkEmail(user);
        if(Checkuser != null) { return 1; }
        return 0;
    }

    /**
     * 이메일 체크
     */
    public User selectByEmail(User user) {
        return userMapper.checkEmail(user);
    }

    /**
     * 회원정보 수정
     */
    public Integer userUpdate(User user) {
        return userMapper.update(user);
    }

    /**
     * 비밀번호 변경
     */
    public int updateUserPassword(String userId, String password){
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setPassword(encodedPassword);
        user.setUserId(userId);
        return userMapper.updateUserPassword(user);
    }
}
