package com.csye6225.webapp.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponseDto {
    private String id;
    @JsonProperty("first_name")
    @JsonAlias("first_name")
    private String firstName;

    @JsonProperty("last_name")
    @JsonAlias("last_name")
    private String lastName;
    private String username;
    private Date accountCreated;
    private Date accountUpdated;
}
