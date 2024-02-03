package com.springoauth.userservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sessions")
public class SessionEntity extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    private LocalDate dateOfIssuance;
    private LocalTime timeOfIssuance;

    @GeneratedValue(strategy = AUTO)
    private UUID sessionToken;
}
