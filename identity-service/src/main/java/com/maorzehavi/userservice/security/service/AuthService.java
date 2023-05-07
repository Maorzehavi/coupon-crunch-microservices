package com.maorzehavi.userservice.security.service;

import com.maorzehavi.userservice.exceptions.SystemException;
import com.maorzehavi.userservice.model.ClientType;
import com.maorzehavi.userservice.model.dto.request.UserRequest;
import com.maorzehavi.userservice.model.dto.response.AuthenticationResponse;
import com.maorzehavi.userservice.security.user.SecurityUser;
import com.maorzehavi.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${api.key}")
    private String apiKey;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(UserRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userService.getEntityByEmail(request.getEmail()).orElseThrow(() -> new SystemException("User not found"));
        var token = jwtService.generateToken(new SecurityUser(user));
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse register( UserRequest request,ClientType clientType){
        var user = userService.createUser(request, clientType).map(userService::mapToUser).orElseThrow();
        var token = jwtService.generateToken(new SecurityUser(user));
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public Long validateToken(final String token) {
        return jwtService.validateToken(token);
    }

    public Boolean validateApiKey(String apikey) {
        return apiKey.equals(apikey);
    }
}
