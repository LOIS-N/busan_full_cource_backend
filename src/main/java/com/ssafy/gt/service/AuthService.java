package com.ssafy.gt.service;

import com.ssafy.gt.dto.User;
import com.ssafy.gt.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthMapper authMapper;

        /**
         * 회원가입
         */
        public User regist(User user) {
                return authMapper.regist(user);
        }

        /**
         * 로그인
         */
        public User login(User user) {
                return authMapper.login(user);
        }

        /**
         * 아이디 조회
         */
        public User selectByUserId(User user) {
                return authMapper.selectByUserId(user);
        }

        /**
         * 이메일 조회
         */
        public User selectByEmail(User user) {
                return authMapper.selectByEmail(user);
        }

        /**
         * 회원정보 수정
         */
        public User userUpdate(User user) {
                return authMapper.update(user);
        }
}
