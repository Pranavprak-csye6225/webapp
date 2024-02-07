package com.csye6225.webapp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "FIRST_NAME", nullable = false)
    @JsonProperty("first_name")
    @JsonAlias("first_name")
    @NotBlank
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    @JsonProperty("last_name")
    @JsonAlias("first_name")
    @NotBlank
    private String lastName;

    @Column(name = "PASSWORD", nullable = false)
    @NotBlank
    private String password;

    @Column(name = "USERNAME", nullable = false,unique=true)
    @NotBlank
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String username;

    @CreationTimestamp
    @Column(name = "ACCOUNT_CREATED")
    private String accountCreated;

    @UpdateTimestamp
    @Column(name = "ACCOUNT_UPDATED")
    private String accountUpdated;


}
