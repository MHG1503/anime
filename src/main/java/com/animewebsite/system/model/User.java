package com.animewebsite.system.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(name = "password",nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String password;

    @Column(name = "verification_code", length = 64,updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String verificationCode;

    private Boolean enabled;

//    @OneToOne(mappedBy = "user")
//    private ForgotPassword forgotPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public List<GrantedAuthority> populateRoles() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase())));
        return authorities;
    }

}
