package com.animewebsite.system.service;

import com.animewebsite.system.dto.MailBody;
import com.animewebsite.system.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMessage(MailBody mailBody){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom(from);
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        javaMailSender.send(message);
    }

    public void sendVerificationEmailForRegister(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String senderName = "Anime MHG";
        String subject = "Xin hay xac nhan dang ky tai khoan!";
        String content = "Xin chao [[name]],<br>"
                + "Xin hay click vao nut ben duoi de xac nhan hoan thanh viec dang ky tai khoan:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Cam on ban!,<br>"
                + "Anime MHG.";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(toAddress);
        helper.setFrom(from,senderName);
        helper.setSubject(subject);

        content = content.replace("[[name]]",user.getUsername());

        String verifyUrl = siteUrl + "/api/auth/verifyRegister?code=" + user.getVerificationCode();
        System.out.println(verifyUrl);
        content = content.replace("[[URL]]",verifyUrl);

        helper.setText(content,true);

        javaMailSender.send(message);
    }
}
