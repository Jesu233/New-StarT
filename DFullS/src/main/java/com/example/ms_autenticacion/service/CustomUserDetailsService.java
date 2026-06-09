package com.example.ms_autenticacion.service;

import com.example.ms_autenticacion.model.User;
import com.example.ms_autenticacion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {

        String runlimpio = input.replace(".", "").replace("-", "").replace(" ", "")
                .toUpperCase();
        User user = userRepository.findById(runlimpio)
                .orElseThrow(()-> new UsernameNotFoundException("No existe"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getRun())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole())
                .build();


    }
}
