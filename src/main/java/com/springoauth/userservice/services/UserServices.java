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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;


@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Service
public class UserServices {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private static final SecretKey authSecretKey = Jwts.SIG.HS256.key().build();
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

        UserEntity currentUserEntity = optionalUser.get();

        if(!this.isPasswordMatching(userDTO.getPassword(),currentUserEntity.getBcryptPassword()))
            throw new IllegalUserFormatException("Password did not match");

        List<SessionEntity> listOfSessionsRegistered
                = this.sessionRepository.countByUserEntity(optionalUser.get().getEntityId());

        if(listOfSessionsRegistered.size() == 3){
            List<DateTimeDTO> listOfLastLogins = new ArrayList<>();
            for (SessionEntity sessionEntity : listOfSessionsRegistered) {
                listOfLastLogins.add(new DateTimeDTO(sessionEntity.getDateOfIssuance(), sessionEntity.getTimeOfIssuance()));
            }

            throw new IllegalUserSessionException("Session Limit exceeded",listOfLastLogins);
        }

        //Pending-state: Register the user session in sessions table, with current date and time.
//        SessionEntity currentSessionEntity =
//            this.sessionRepository.save(new SessionEntity(currentUserEntity,LocalDate.now(),LocalTime.now(), UUID.randomUUID()));
//
//        return new SessionDTO(currentSessionEntity.getSessionToken(),currentSessionEntity.getDateOfIssuance(),currentSessionEntity.getTimeOfIssuance());

        /*A way to create JSON - Create Map with key,values pairs and then creating claims out of them. Compact means stringify the claim.*/

        Map<String, Object> jsonPayloadMap = new HashMap<>();
        jsonPayloadMap.put("username", userDTO.getUsername());
        jsonPayloadMap.put("dateOfSessionRegistration", String.valueOf(LocalDate.now()));
        LocalTime currTime = LocalTime.now();
        jsonPayloadMap.put("timeOfSessionRegistered", String.valueOf(currTime));

        String authTokenGenerated = Jwts.builder()
                .claims(jsonPayloadMap).signWith(authSecretKey).compact();
        SessionEntity sessionGenerated = this.sessionRepository.save(new SessionEntity(currentUserEntity,LocalDate.now(),currTime,authTokenGenerated, 0));

        return SessionDTO.builder()
                .sessionToken(sessionGenerated.getToken())
                .dateOfSessionRegistered(sessionGenerated.getDateOfIssuance())
                .timeOfSessionRegistered(sessionGenerated.getTimeOfIssuance())
                .build();
    }

    public boolean signOut(UserDTO userDTO,String authHeaderToken) throws IllegalUserFormatException, IllegalUserSessionException{
        if(userDTO == null)                     throw new IllegalUserFormatException("Illegal user request body found.");
        if(authHeaderToken == null)             throw new IllegalUserFormatException("No header auth token found");

        Optional<UserEntity> optionalUser = this.userRepository.findByUsernameEqualsIgnoreCase(userDTO.getUsername());
        if(optionalUser.isEmpty())              throw new IllegalUserFormatException("User doesn't exist in the repository!");

        UserEntity currentUser = optionalUser.get();
        Optional<SessionEntity> optionalSessionEntity = this.sessionRepository.findByUserEntityAndAndToken(currentUser,authHeaderToken);

        if(optionalSessionEntity.isEmpty())     throw new IllegalUserSessionException("Username and token do not match");
        SessionEntity sessionEntity = optionalSessionEntity.get();

        sessionEntity.setSessionStatus(1);
        this.sessionRepository.save(sessionEntity);


//        List<SessionEntity> listOfSessionsRegistered
//                = this.sessionRepository.countByUserEntity(currentUser.getEntityId());
//        SessionEntity lastSession = listOfSessionsRegistered.get(listOfSessionsRegistered.size()-1);

//        this.sessionRepository.deleteBySessionTokenAndUserEntity(lastSession.getSessionToken(),lastSession.getUserEntity());

        return true;
    }

    public int validate(String authHeaderToken) throws IllegalUserSessionException{
        if(authHeaderToken == null)     throw new IllegalUserSessionException("Authentication Header Token not found!");

        Optional<SessionEntity> optionalSessionEntity = this.sessionRepository.findByToken(authHeaderToken);
        if(optionalSessionEntity.isEmpty())     throw new IllegalUserSessionException("No session token found!");

        SessionEntity sessionEntity = optionalSessionEntity.get();
        return sessionEntity.getSessionStatus();
    }

    private String getBCryptPassword(String text){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(text);
    }

    private boolean isPasswordMatching(String password,String bcryptPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password,bcryptPassword);
    }
}
