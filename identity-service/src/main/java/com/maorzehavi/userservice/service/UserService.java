package com.maorzehavi.userservice.service;


import com.maorzehavi.userservice.exceptions.SystemException;
import com.maorzehavi.userservice.model.ClientType;
import com.maorzehavi.userservice.model.dto.request.UserRequest;
import com.maorzehavi.userservice.model.dto.response.UserResponse;
import com.maorzehavi.userservice.model.entity.User;
import com.maorzehavi.userservice.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       @Lazy RoleService roleService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean isEmailTaken(Long userId, String email) {
        var user = userRepository.findByEmail(email);
        return user.filter(value -> !value.getId().equals(userId)).isPresent();
    }

    public Optional<UserResponse> getByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToUserResponse);
    }

    public UserResponse getById(Long id) {
        return userRepository.findById(id).map(this::mapToUserResponse).orElseThrow(
                () -> new SystemException("Not user found with id '" + id + "'")
        );
    }

    public Optional<UserResponse> createUser(UserRequest userRequest, ClientType clientType) {
        if (userRepository.existsByEmail(userRequest.getEmail()))
            throw new SystemException("Email: " + userRequest.getEmail() + " is taken");
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        var roles = roleService.getAllRolesEntityByClientType(clientType);
        if (roles.isPresent() && roles.get().size() > 0) {
            var user = mapToUser(userRequest);
            user.setClientType(clientType);
            user.setRoles(roles.get());
            return Optional.of(mapToUserResponse(userRepository.save(user)));
        }
        throw new SystemException("Can not find roles for client type " + clientType);
    }

    public Optional<UserResponse> updateUser(Long id, UserRequest userRequest) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setEmail(userRequest.getEmail());
            user.get().setPassword(passwordEncoder.encode(userRequest.getPassword()));
            return Optional.of(mapToUserResponse(userRepository.save(user.get())));
        }
        throw new SystemException("User with id: " + id + " not found");
    }

    public Optional<UserResponse> deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new SystemException("Not user found with id '" + id + "'")
        );
        userRepository.delete(user);
        return Optional.of(mapToUserResponse(user));
    }

    public Optional<User> getEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserResponse).toList();
    }

    public UserResponse mapToUserResponse(User user) {
        var roles = user.getRoles().stream().map(roleService::mapToRoleResponse).collect(Collectors.toSet());
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .clientType(user.getClientType())
                .roles(roles)
                .build();
    }

    public User mapToUser(UserResponse userResponse) {
        var roles = userResponse.getRoles().stream().map(roleService::mapToRole).collect(Collectors.toSet());
        return User.builder()
                .id(userResponse.getId())
                .email(userResponse.getEmail())
                .clientType(userResponse.getClientType())
                .roles(roles)
                .build();
    }

    public User mapToUser(UserRequest userRequest) {
        return User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }
}