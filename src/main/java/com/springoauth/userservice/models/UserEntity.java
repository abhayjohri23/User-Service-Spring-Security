package com.springoauth.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserEntity extends BaseEntity{
    private String username;
    private String bcryptPassword;

    @OneToMany(mappedBy = "userEntity",fetch = FetchType.LAZY)
    private List<SessionEntity> listOfSessionsIssued;
}
