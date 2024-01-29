package com.springoauth.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class DateTimeDTO {
    private Date dateOfSession;
    private Time timeOfSession;
}
