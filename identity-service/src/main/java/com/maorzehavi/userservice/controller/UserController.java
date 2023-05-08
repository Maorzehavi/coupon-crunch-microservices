package com.maorzehavi.userservice.controller;

import com.maorzehavi.userservice.model.ClientType;
import com.maorzehavi.userservice.model.dto.request.UserRequest;
import com.maorzehavi.userservice.model.dto.response.UserResponse;
import com.maorzehavi.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Validated UserRequest userRequest,
                                           @RequestParam ClientType clientType) {
        var userResponse = userService.createUser(userRequest, clientType).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with name " + userRequest.getEmail() + " already exists"));
        return ResponseEntity.created(URI.create("/api/v1/users/" + userResponse.getId())).body(userResponse.getId());
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @RequestBody @Validated UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id)
        ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id)
        ));
    }
}
