package com.maorzehavi.userservice.model.entity;

import com.maorzehavi.userservice.model.ClientType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}
