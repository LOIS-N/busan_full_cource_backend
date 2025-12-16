package com.ssafy.gt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenVerifyRequest {
    @NotBlank
    private String token;
}
