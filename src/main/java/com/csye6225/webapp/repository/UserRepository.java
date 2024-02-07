package com.csye6225.webapp.repository;

import com.csye6225.webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

}
