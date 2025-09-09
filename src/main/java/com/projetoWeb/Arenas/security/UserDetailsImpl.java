package com.projetoWeb.Arenas.security;

import com.projetoWeb.Arenas.service.UserService;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImpl  implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userService.getUserByEmail(username);
        } catch (EntityNotExistsException entityNotExistsException) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }
    }
}