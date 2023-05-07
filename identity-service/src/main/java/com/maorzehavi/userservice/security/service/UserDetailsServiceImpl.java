package com.maorzehavi.userservice.security.service;

import com.maorzehavi.userservice.security.user.SecurityUser;
import com.maorzehavi.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.getEntityByEmail(username);
        if (user.isPresent()) {
            return user.map(SecurityUser::new)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}