package com.animewebsite.system.repository;

import com.animewebsite.system.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.password =:newPassword WHERE u =:user")
    void updateUserPassword(@Param("newPassword") String newPassword, @Param("user") User user);

    @Query("SELECT u FROM User u WHERE u.verificationCode =:verificationCode")
    Optional<User> findByVerificationCode(@Param("verificationCode") String verificationCode);
}
