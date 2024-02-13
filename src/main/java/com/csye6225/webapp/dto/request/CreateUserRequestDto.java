package com.csye6225.webapp.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserRequestDto {
    private String username;
    @JsonProperty("first_name")
    @JsonAlias("first_name")
    private String firstName;

    @JsonProperty("last_name")
    @JsonAlias("last_name")
    private String lastName;

    private String password;


}
