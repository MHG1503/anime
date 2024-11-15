package com.animewebsite.system.repository;

import com.animewebsite.system.model.ForgotPassword;
import com.animewebsite.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Long> {

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp =:otp AND fp.user =:user")
    Optional<ForgotPassword> findByOtpAndUser(@Param("otp") Integer otp, @Param("user")User user);

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.user =:user")
    Optional<ForgotPassword> findByUser(User user);
}
