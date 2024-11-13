package com.uniquindio.api_rest.infra.security.model;

import com.uniquindio.api_rest.model.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String username, password;
    private int codigo;
    private Collection<? extends GrantedAuthority> authorities;
    public static UserDetailsImpl build(Cuenta user){

        List<GrantedAuthority> authorities = new ArrayList<>();


        authorities.add( new SimpleGrantedAuthority("USER") );

        return new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getId(), authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
