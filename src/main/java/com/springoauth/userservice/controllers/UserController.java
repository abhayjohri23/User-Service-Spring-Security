package com.springoauth.userservice.controllers;

import com.springoauth.userservice.dtos.SessionDTO;
import com.springoauth.userservice.dtos.UserDTO;
import com.springoauth.userservice.exceptions.IllegalUserFormatException;
import com.springoauth.userservice.exceptions.IllegalUserSessionException;
import com.springoauth.userservice.services.UserServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServices userServices;
    public UserController(UserServices userServices){
        this.userServices = userServices;
    }

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody UserDTO userDTO) throws IllegalUserFormatException {
        return this.userServices.signUp(userDTO);
    }

    @PostMapping("/login")
    public SessionDTO signIn(@RequestBody UserDTO userDTO) throws IllegalUserFormatException, IllegalUserSessionException {
        return this.userServices.signIn(userDTO);
    }

    @PostMapping("/logout")
    public UserDTO signOut(@RequestBody UserDTO userDTO) throws IllegalUserFormatException, IllegalUserSessionException{
        return this.userServices.signOut(userDTO);
    }
}
