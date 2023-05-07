package com.maorzehavi.userservice.repository;

import com.maorzehavi.userservice.model.ClientType;
import com.maorzehavi.userservice.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> getByRole(String role);
    Optional<Set<Role>> getAllByRoleIn(Set<String> roles);
    Boolean existsByRole(String role);
    @Query("select r.id from Role r where r.role = ?1")
    Optional<Long> getIdByRole(String role);
    @Query("select r.role from Role r where r.id = ?1")
    Optional<String> getRoleById(Long id);
    @Query("select r from Role r where r.clientType = ?1")
    Optional<Set<Role>>findAllByClientType(ClientType clientType);

    Optional<Role> findByRole(String role);
}