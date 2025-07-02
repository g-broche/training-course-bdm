package com.example.bdm.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bdm.model.AppUser;
import com.example.bdm.repository.AppUserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    public CustomUserDetailsService(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String formatedRole = "ROLE_" + user.getRole().getName().toUpperCase();
        System.out.println(">>> role authority : "+formatedRole);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(formatedRole);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(authority)
                );
    }
}

