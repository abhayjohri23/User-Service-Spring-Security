package com.springoauth.userservice.dtos;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDTO {
    private String sessionToken;
    private LocalDate dateOfSessionRegistered;
    private LocalTime timeOfSessionRegistered;
}
