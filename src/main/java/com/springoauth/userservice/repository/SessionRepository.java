package com.springoauth.userservice.repository;

import com.springoauth.userservice.models.SessionEntity;
import com.springoauth.userservice.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity,Long> {
    //Sign In: First check the count of sessions registered with the user. If lesser than 3, register one. Else return falsy value
    @Query(value ="SELECT * FROM sessions WHERE user_id = :userId",nativeQuery = true)
    List<SessionEntity> countByUserEntity(Long userId);

    //Sign Out: By Session Token, we can delete the session entry and return the session details of the deleted tuple.
    void deleteBySessionTokenAndUserEntity(UUID sessionToken, UserEntity userEntity);
}
