package com.ssafy.gt.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class UserResponse {
    private int id;
    private String userId;
    private String nickname;
    private String email;
}