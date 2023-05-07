package com.maorzehavi.userservice.repository;

import com.maorzehavi.userservice.model.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> getByAuthority(String authority);

    @Query("select a.id from Authority a where a.authority = ?1")
    Optional<Long> getIdByAuthority(String authority);

    @Query("select a from Authority a where a.authority in ?1")
    Optional<Set<Authority>> getAllByAuthorityIn(Set<String> authorities);

    Boolean existsByAuthority(String authority);
}