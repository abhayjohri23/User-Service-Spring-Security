package com.springoauth.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class DateTimeDTO {
    private LocalDate dateOfSession;
    private LocalTime timeOfSession;
}
