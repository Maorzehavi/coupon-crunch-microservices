package com.maorzehavi.userservice.service;

import com.maorzehavi.userservice.exceptions.SystemException;
import com.maorzehavi.userservice.model.dto.request.AuthorityRequest;
import com.maorzehavi.userservice.model.dto.response.AuthorityResponse;
import com.maorzehavi.userservice.model.entity.Authority;
import com.maorzehavi.userservice.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityService  {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Boolean existsByAuthority(String authority) {
        return authorityRepository.existsByAuthority(authority);
    }

    public Optional<Long> getIdByAuthority(String authority) {
        return authorityRepository.getIdByAuthority(authority);
    }

    public Optional<AuthorityResponse> getAuthority(Long id) {
        return authorityRepository.findById(id).map(this::mapToAuthorityResponse);
    }

    public Optional<Authority> getAuthorityEntity(Long id) {
        return authorityRepository.findById(id);
    }

    @Transactional
    public Optional<AuthorityResponse> createAuthority(AuthorityRequest authority) {
        if (authorityRepository.existsByAuthority(authority.getAuthority()))
            throw new SystemException("Authority already exists");
        var a = authorityRepository.save(mapToAuthority(authority));
        return Optional.of(mapToAuthorityResponse(a));

    }

    public Optional<AuthorityResponse> updateAuthority(Long id, AuthorityRequest authority) {
        return Optional.empty();
    }

    public Optional<AuthorityResponse> deleteAuthority(Long id) {
        var a = authorityRepository.findById(id).orElseThrow(() -> new SystemException("Authority not found"));
        authorityRepository.deleteById(id);
        return Optional.of(mapToAuthorityResponse(a));
    }

    public List<AuthorityResponse> getAllAuthorities() {
        return authorityRepository.findAll().stream().map(this::mapToAuthorityResponse).collect(Collectors.toList());
    }

    public Optional<AuthorityResponse> getByAuthority(String authority) {
        return authorityRepository.getByAuthority(authority).map(this::mapToAuthorityResponse);
    }

    public Optional<List<AuthorityResponse>> getAllByAuthorityIn(Set<String> authorities) {
        return authorityRepository.getAllByAuthorityIn(authorities)
                .map(authorities1 -> authorities1.stream()
                        .map(this::mapToAuthorityResponse)
                        .collect(Collectors.toList()));
    }

    public AuthorityResponse mapToAuthorityResponse(Authority authority) {
        return AuthorityResponse.builder()
                .id(authority.getId())
                .authority(authority.getAuthority())
                .build();
    }

    public AuthorityResponse mapToAuthorityResponse(AuthorityRequest authorityRequest) {
        return AuthorityResponse.builder()
                .authority(authorityRequest.getAuthority())
                .build();
    }

    public Authority mapToAuthority(AuthorityResponse authorityResponse) {
        return Authority.builder()
                .id(authorityResponse.getId())
                .authority(authorityResponse.getAuthority())
                .build();
    }

    public Authority mapToAuthority(AuthorityRequest authorityRequest) {
        return Authority.builder()
                .authority(authorityRequest.getAuthority())
                .build();
    }

}
