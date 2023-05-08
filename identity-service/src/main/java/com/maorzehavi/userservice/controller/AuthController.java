package com.maorzehavi.userservice.controller;

import com.maorzehavi.userservice.model.dto.request.UserRequest;
import com.maorzehavi.userservice.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest request){
        try{
            return ResponseEntity.ok((authService.authenticate(request)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        try{
            authService.logout();
            return ResponseEntity.ok("Logout successful");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody String token){
        try{
            return ResponseEntity.ok(authService.validateToken(token));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate/apikey")
    public ResponseEntity<?> validateApiKey(@RequestBody String token){
        try{
            System.out.println("token: " + token);
            return ResponseEntity.ok(authService.validateApiKey(token));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        try{
            return ResponseEntity.ok("test");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
