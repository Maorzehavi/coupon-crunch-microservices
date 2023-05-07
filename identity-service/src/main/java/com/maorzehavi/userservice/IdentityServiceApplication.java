package com.maorzehavi.userservice;

import com.maorzehavi.userservice.model.ClientType;
import com.maorzehavi.userservice.model.dto.request.AuthorityRequest;
import com.maorzehavi.userservice.model.dto.request.RoleRequest;
import com.maorzehavi.userservice.model.dto.request.UserRequest;
import com.maorzehavi.userservice.service.RoleService;
import com.maorzehavi.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
@EnableDiscoveryClient
public class IdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }

//
//    @Bean
//    public CommandLineRunner demo(RoleService roleService, UserService userService) {
//        // TODO: remove this method this can run only once
//        return (args) -> {
//            roleService.createRole(RoleRequest.builder()
//                    .role("admin")
//                    .clientType(ClientType.ADMINISTRATOR)
//                    .authorities(Set.of(
//                            AuthorityRequest.builder()
//                                    .authority("crud:user")
//                                    .build(), AuthorityRequest.builder()
//                                    .authority("crud:customer")
//                                    .build(),
//                            AuthorityRequest.builder()
//                                    .authority("crud:company")
//                                    .build()
//                    )).build());
//            roleService.createRole(RoleRequest.builder()
//                    .role("company")
//                    .clientType(ClientType.COMPANY)
//                    .authorities(Set.of(
//                            AuthorityRequest.builder()
//                                    .authority("crud:coupon")
//                                    .build()
//                    )).build());
//            roleService.createRole(RoleRequest.builder()
//                    .role("customer")
//                    .clientType(ClientType.CUSTOMER)
//                    .authorities(Set.of(
//                            AuthorityRequest.builder()
//                                    .authority("get:coupon")
//                                    .build()
//                    )).build());
//            UserRequest userRequest = UserRequest.builder()
//                    .email("root@root.com")
//                    .password("root")
//                    .build();
//            userService.createUser(userRequest, ClientType.ADMINISTRATOR);
//
//        };
//    }

}
