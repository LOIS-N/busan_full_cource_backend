package com.ssafy.gt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("비밀번호 재설정 요청");
        message.setText(
            "비밀번호 재설정을 요청하셨습니다. \n\n" +
            "다음 링크를 클릭하여 비밀번호를 재설정하세요:\n" +
            resetUrl + "\n\n" +
            "이 링크는 30분 동안 유효합니다. \n" +
            "요청하지 않으셨다면 이 이메일을 무시하세요."
        );

        mailSender.send(message);
    }
}
