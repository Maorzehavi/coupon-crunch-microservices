package com.maorzehavi.userservice.service;


import com.maorzehavi.userservice.exceptions.SystemException;
import com.maorzehavi.userservice.model.ClientType;
import com.maorzehavi.userservice.model.dto.request.RoleRequest;
import com.maorzehavi.userservice.model.dto.response.RoleResponse;
import com.maorzehavi.userservice.model.entity.Role;
import com.maorzehavi.userservice.repository.RoleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService  {

    private final RoleRepository roleRepository;
    private final AuthorityService authorityService;

    public RoleService(RoleRepository roleRepository,
                           @Lazy AuthorityService authorityService) {
        this.roleRepository = roleRepository;
        this.authorityService = authorityService;
    }

    public Boolean existsByRole(String role) {
        return roleRepository.existsByRole(role);
    }

    public Optional<Long> getIdByRole(String role) {
        return roleRepository.getIdByRole(role);
    }

    public Optional<RoleResponse> getRole(Long id) {
        return roleRepository.findById(id).map(this::mapToRoleResponse);
    }

    public Optional<RoleResponse> getRole(String role) {
        return roleRepository.findByRole(role).map(this::mapToRoleResponse);
    }

    public Optional<Set<RoleResponse>> getAllRoles() {
        return Optional.of(roleRepository.findAll().stream().map(this::mapToRoleResponse).collect(Collectors.toSet()));
    }

    @Transactional
    @Modifying
    public Optional<RoleResponse> createRole(RoleRequest roleRequest) {
        if (roleRequest.getRole().startsWith("ROLE_")) {
            roleRequest.setRole(roleRequest.getRole().toUpperCase());
        } else {
            roleRequest.setRole("ROLE_" + roleRequest.getRole().toUpperCase());
        }
        if (roleRepository.existsByRole(roleRequest.getRole())) {

            throw new SystemException("Role '" + roleRequest.getRole() + "' already exists");
        }

        var authorities = roleRequest.getAuthorities().stream().map(authorityRequest -> {
            if (authorityRequest.getAuthority().startsWith("SCOPE_")) {
                authorityRequest.setAuthority(authorityRequest.getAuthority().toUpperCase());
            } else {
                authorityRequest.setAuthority("SCOPE_" + authorityRequest.getAuthority().toUpperCase());
            }
            if (!authorityService.existsByAuthority(authorityRequest.getAuthority())) {
                authorityService.createAuthority(authorityRequest);
            }
            return authorityService.getByAuthority(authorityRequest.getAuthority()).map(authorityService::mapToAuthority).orElseThrow(() -> new SystemException("Authority not found"));
        }).collect(Collectors.toSet());
        var role = Role.builder()
                .role(roleRequest.getRole())
                .clientType(roleRequest.getClientType())
                .authorities(authorities).build();
        roleRepository.save(role);
        return Optional.of(mapToRoleResponse(role));
    }

    @Transactional
    @Modifying
    public Optional<RoleResponse> updateRole(Long id, RoleRequest roleRequest) {
        if (roleRequest.getRole().startsWith("ROLE_")) {
            roleRequest.setRole(roleRequest.getRole().toUpperCase());
        } else {
            roleRequest.setRole("ROLE_" + roleRequest.getRole().toUpperCase());
        }
        var optionalRole = roleRepository.getByRole(roleRequest.getRole());
        if (optionalRole.isPresent() && !optionalRole.get().getId().equals(id)) {
            throw new SystemException("Role '" + roleRequest.getRole() + "' already exists");
        }
        var role = roleRepository.findById(id).orElseThrow(() -> new SystemException("Role with id '" + id + "' not found"));
        var authorities = roleRequest.getAuthorities().stream().map(authorityRequest -> {
            if (authorityRequest.getAuthority().startsWith("SCOPE_")) {
                authorityRequest.setAuthority(authorityRequest.getAuthority().toUpperCase());
            } else {
                authorityRequest.setAuthority("SCOPE_" + authorityRequest.getAuthority().toUpperCase());
            }
            if (!authorityService.existsByAuthority(authorityRequest.getAuthority())) {
                authorityService.createAuthority(authorityRequest);
            }
            return authorityService.getByAuthority(authorityRequest.getAuthority()).map(authorityService::mapToAuthority).orElseThrow(() -> new SystemException("Authority not found"));
        }).collect(Collectors.toSet());
        role.setRole(roleRequest.getRole());
        role.setClientType(roleRequest.getClientType());
        role.setAuthorities(authorities);
        roleRepository.save(role);
        return Optional.of(mapToRoleResponse(role));

    }

    @Transactional
    @Modifying
    public Optional<RoleResponse> deleteRole(Long id) {
        var role = roleRepository.findById(id).orElseThrow(() -> new SystemException("Role not found"));
        roleRepository.delete(role);
        return Optional.of(mapToRoleResponse(role));
    }

    public Optional<Set<Role>> getAllRolesEntityByClientType(ClientType clientType) {
        return roleRepository.findAllByClientType(clientType);
    }


    public RoleResponse mapToRoleResponse(RoleRequest roleRequest) {
        var authorities = roleRequest.getAuthorities().stream().map(authorityService::mapToAuthorityResponse).collect(Collectors.toSet());
        return RoleResponse.builder()
                .role(roleRequest.getRole())
                .authorities(authorities)
                .build();
    }

    public RoleResponse mapToRoleResponse(Role role) {
        var authorities = role.getAuthorities().stream()
                .map(authorityService::mapToAuthorityResponse).collect(Collectors.toSet());
        return RoleResponse.builder()
                .id(role.getId())
                .role(role.getRole())
                .clientType(role.getClientType())
                .authorities(authorities)
                .build();
    }

    public Role mapToRole(RoleRequest roleRequest) {
        return Role.builder()
                .role(roleRequest.getRole())
                .clientType(roleRequest.getClientType())
                .build();
    }

    public Role mapToRole(RoleResponse roleResponse) {
        var authorities = roleResponse.getAuthorities().stream()
                .map(authorityService::mapToAuthority).collect(Collectors.toSet());
        return Role.builder()
                .id(roleResponse.getId())
                .role(roleResponse.getRole())
                .authorities(authorities)
                .clientType(roleResponse.getClientType())
                .build();
    }

}