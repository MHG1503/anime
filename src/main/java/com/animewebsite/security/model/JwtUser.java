package com.animewebsite.security.model;


import com.animewebsite.system.model.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.getEnabled();
        this.authorities = user.populateRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
