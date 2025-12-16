package com.ssafy.gt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordChangeRequest {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}
