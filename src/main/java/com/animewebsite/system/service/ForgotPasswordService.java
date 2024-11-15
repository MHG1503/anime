package com.animewebsite.system.service;

import com.animewebsite.system.dto.MailBody;
import com.animewebsite.system.dto.req.ResetPasswordRequest;
import com.animewebsite.system.model.ForgotPassword;
import com.animewebsite.system.model.User;
import com.animewebsite.system.repository.ForgotPasswordRepository;
import com.animewebsite.system.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String verifyEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Khong tim thay email!"));

        Optional<ForgotPassword> forgotPasswordOptional = forgotPasswordRepository
                .findByUser(user);

        forgotPasswordOptional.ifPresent(forgotPassword -> forgotPasswordRepository.deleteById(forgotPassword.getId()));

        Integer otp = generateOtp();
        MailBody mailBody = MailBody
                .builder()
                .to(email)
                .subject("OTP cho yeu cau quen mat khau!")
                .text("Day la OTP cua ban: " + otp)
                .build();

        ForgotPassword forgotPassword = ForgotPassword
                .builder()
                .otp(otp)
                .user(user)
                .expirationDate(new Date(System.currentTimeMillis() + 70 * 1000))
                .build();

        forgotPasswordRepository.save(forgotPassword);
        emailService.sendMessage(mailBody);
        return "OTP da duoc gui den email cua ban. Vui long kiem tra lai email!";
    }

    @Transactional
    public String verifyOtp(String email, Integer otp){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Khong tim thay email!"));

        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp,user)
                .orElseThrow(()-> new RuntimeException("OTP khong hop le!"));

        if(forgotPassword.getExpirationDate().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getId());
            throw new RuntimeException("OTP da het han");
        }

        return "OTP da duoc xac nhan!";
    }

    @Transactional
    public String resetPassword(ResetPasswordRequest resetPasswordRequest, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Khong tim thay email!"));

        String newPassword = resetPasswordRequest.newPassword();
        String confirmPassword = resetPasswordRequest.confirmPassword();
        if(!Objects.equals(newPassword,confirmPassword)){
            throw new RuntimeException("Mat khau khong hop le, vui long nhap lai mat khau");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        userRepository.updateUserPassword(encodedPassword,user);

        return "Mat khau thay doi thanh cong";
    }

    private Integer generateOtp(){
        return new Random().nextInt(100_000,999_999);
    }
}
