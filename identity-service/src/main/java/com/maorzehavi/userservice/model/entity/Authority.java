package com.maorzehavi.userservice.model.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "authority")
@ToString
@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String authority;

    @ManyToMany(mappedBy = "authorities")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private Set<Role> roles;
}