package com.springoauth.userservice.security;

import com.springoauth.userservice.models.UserEntity;
import com.springoauth.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsernameEqualsIgnoreCase(username);
        if(optionalUserEntity.isEmpty())        throw new UsernameNotFoundException("Username not found!");

        UserEntity userEntity = optionalUserEntity.get();
        return new CustomUserDetails(userEntity);
    }
}
