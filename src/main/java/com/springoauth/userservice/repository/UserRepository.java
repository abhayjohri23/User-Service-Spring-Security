package com.springoauth.userservice.repository;

import com.springoauth.userservice.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepo")
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    //To be used in deletion of the user from database:
    void deleteByUsernameEqualsIgnoreCase(String username);
    Optional<UserEntity> findByUsernameEqualsIgnoreCase(String username);

    //To be used in sinUp and signIn feature.
    boolean existsByUsername(String username);

//    @Override - For user creation
//    <S extends UserEntity> S save(S entity);
}
