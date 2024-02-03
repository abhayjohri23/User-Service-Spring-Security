package com.springoauth.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
    private UUID sessionToken;
    private LocalDate dateOfSessionRegistered;
    private LocalTime timeOfSessionRegistered;
}
