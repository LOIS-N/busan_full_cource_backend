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
        public Integer regist(User user) {
                return authMapper.regist(user);
        }

        /**
         * 로그인
         */
        public User login(User user) {
                return authMapper.login(user);
        }

        /**
         * 회원정보 조회
         */
        public User selectByUserId(User user) {
                return authMapper.selectByUserId(user);
        }

        /**
         * 아이디 중복 검사
         */
        public Integer checkUserId(User user) {
                User Checkuser = authMapper.checkUserId(user);
                if(Checkuser != null) { return 1; }
                return 0;
        }

        /**
         * 이메일 중복 검사
         */
        public Integer checkEmail(User user) {
                User Checkuser = authMapper.checkEmail(user);
                if(Checkuser != null) { return 1; }
                return 0;
        }

        /**
         * 회원정보 수정
         */
        public Integer userUpdate(User user) {
                return authMapper.update(user);
        }
}
