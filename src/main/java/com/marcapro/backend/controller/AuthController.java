package com.marcapro.backend.controller;

import com.marcapro.backend.dto.AuthDto;
import com.marcapro.backend.entity.AdminUser;
import com.marcapro.backend.repository.AdminUserRepository;
import com.marcapro.backend.security.AdminUserDetailsService;
import com.marcapro.backend.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminUserDetailsService userDetailsService;
    private final AdminUserRepository adminUserRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthDto.LoginResponse> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtService.generateToken(userDetails);
        AdminUser user = adminUserRepository.findByUsername(request.username()).orElseThrow();
        return ResponseEntity.ok(new AuthDto.LoginResponse(token, user.getUsername(), user.getFullName()));
    }
}
