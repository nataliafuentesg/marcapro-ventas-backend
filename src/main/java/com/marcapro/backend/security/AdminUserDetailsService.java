package com.marcapro.backend.security;

import com.marcapro.backend.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminUserRepository.findByUsername(username)
            .filter(com.marcapro.backend.entity.AdminUser::isActive)
            .map(u -> User.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .roles("ADMIN")
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
