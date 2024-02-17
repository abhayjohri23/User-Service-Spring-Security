package com.springoauth.userservice.controllers;

import com.springoauth.userservice.dtos.SessionDTO;
import com.springoauth.userservice.dtos.UserDTO;
import com.springoauth.userservice.exceptions.IllegalUserFormatException;
import com.springoauth.userservice.exceptions.IllegalUserSessionException;
import com.springoauth.userservice.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    public ResponseEntity<SessionDTO> signIn(@RequestBody UserDTO userDTO) throws IllegalUserFormatException, IllegalUserSessionException {
        return this.userServices.signIn(userDTO);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> signOut(@RequestBody UserDTO userDTO, @RequestHeader(AUTHORIZATION) String authToken) throws IllegalUserFormatException, IllegalUserSessionException{
        ResponseEntity<Boolean> responseEntity = this.userServices.signOut(userDTO,authToken);
        if(Boolean.FALSE.equals(responseEntity.getBody()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(userDTO.getUsername() +" was not logged out!");
        else
            return new ResponseEntity<String>(userDTO.getUsername() + " logged out successfully!",HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<Integer> validate(@RequestHeader(AUTHORIZATION) String authHeaderToken,@RequestBody UserDTO userDTO) throws IllegalUserSessionException{
        int sessionStatus = this.userServices.validate(authHeaderToken, userDTO.getUsername());
        return new ResponseEntity<Integer>(sessionStatus,(sessionStatus == 0) ? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE);
    }
}
