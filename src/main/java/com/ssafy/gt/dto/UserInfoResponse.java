package com.ssafy.gt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private int id;
    private String userId;
    private String nickname;
    private String email;
}