package com.springoauth.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
    private String sessionToken;
    private String dateOfSessionRegistered;
    private String timeOfSessionRegistered;
}
