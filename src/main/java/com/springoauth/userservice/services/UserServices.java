package com.springoauth.userservice.services;

import com.springoauth.userservice.dtos.DateTimeDTO;
import com.springoauth.userservice.dtos.SessionDTO;
import com.springoauth.userservice.dtos.UserDTO;
import com.springoauth.userservice.exceptions.IllegalUserFormatException;
import com.springoauth.userservice.exceptions.IllegalUserSessionException;
import com.springoauth.userservice.models.SessionEntity;
import com.springoauth.userservice.models.UserEntity;
import com.springoauth.userservice.repository.SessionRepository;
import com.springoauth.userservice.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Service
public class UserServices {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    public UserServices(UserRepository userRepository,SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }
    private UserDTO getUserDTOFromUserEntity(UserEntity userEntity){
        if(userEntity == null)          return null;

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setPassword(userEntity.getBcryptPassword());

        return userDTO;
    }

    private UserEntity getUserEntityFromUserDTO(UserDTO userDTO){
        if(userDTO == null)         return null;
        UserEntity user = new UserEntity();
        user.setUsername(userDTO.getUsername());
        user.setBcryptPassword(getBCryptPassword(userDTO.getPassword()));

        return user;
    }
    public UserDTO signUp(UserDTO userDTO) throws IllegalUserFormatException {
        UserEntity userEntity = getUserEntityFromUserDTO(userDTO);
        if(!this.userRepository.existsByUsername(userEntity.getUsername()))
            this.userRepository.save(userEntity);
        else
            throw new IllegalUserFormatException("User already exist in the database");
        return userDTO;
    }

    public SessionDTO signIn(UserDTO userDTO) throws IllegalUserFormatException, IllegalUserSessionException {
        if(userDTO == null)
            throw new IllegalUserFormatException("Illegal user request body found.");

        Optional<UserEntity> optionalUser = this.userRepository.findByUsernameEqualsIgnoreCase(userDTO.getUsername());
        if(optionalUser.isEmpty())        throw new IllegalUserFormatException("User doesn't exist in the repository!");

        List<SessionEntity> listOfSessionsRegistered
                = this.sessionRepository.countByUserEntity(optionalUser.get().getEntityId());

        List<DateTimeDTO> listOfLastLogins = new ArrayList<>();
        for (SessionEntity sessionEntity : listOfSessionsRegistered) {
            listOfLastLogins.add(new DateTimeDTO(sessionEntity.getDateOfIssuance(), sessionEntity.getTimeOfIssuance()));
        }

        if(listOfSessionsRegistered.size() == 3)
            throw new IllegalUserSessionException("Session Limit exceeded",listOfLastLogins);

        //Pending-state: Register the user session in sessions table, with current date and time.
        return null;
    }

    public UserDTO signOut(UserDTO userDTO){
        return null;
    }

    private String getBCryptPassword(String text){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(text);
    }
}
