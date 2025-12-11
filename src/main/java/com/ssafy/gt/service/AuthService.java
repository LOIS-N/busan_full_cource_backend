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

        /**
         * 아이디 중복 조화
         */

        /**
         * 이메일 중복 조회
         */

        /**
         * 회원 정보 조회
         */
}
