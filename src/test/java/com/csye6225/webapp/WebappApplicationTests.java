package com.csye6225.webapp;

import com.csye6225.webapp.dto.request.CreateUserRequestDto;
import com.csye6225.webapp.dto.request.UpdateUserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebappApplicationTests {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void createGetSuccessTest() throws Exception {
        CreateUserRequestDto user = new CreateUserRequestDto("pranav@gmail.com", "Pranav", "Prakash", "pas");
        String json = mapper.writeValueAsString(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("pranav@gmail.com", "pas");
        this.mockMvc.perform(post("/v1/user").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
        this.mockMvc.perform(get("/v1/user/self")
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(headers))
                        .andExpect(status().isOk())
                        .andExpect(content()
                           .json("{'username':'pranav@gmail.com','first_name':'Pranav','last_name':'Prakash'}"));
    }

    @Test
    @Order(2)
    void updateGetSuccessTest() throws Exception {
        UpdateUserRequestDto user = new UpdateUserRequestDto("FirstName", "LastName", "password");
        String json = mapper.writeValueAsString(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("pranav@gmail.com", "pas");
        this.mockMvc.perform(put("/v1/user/self").content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers))
                        .andExpect(status().isNoContent());
        headers.setBasicAuth("pranav@gmail.com", "password");
        this.mockMvc.perform(get("/v1/user/self")
                        .accept(MediaType.APPLICATION_JSON)
                        .headers(headers))
                        .andExpect(status().isOk())
                        .andExpect(content()
                            .json("{'username':'pranav@gmail.com','first_name':'FirstName','last_name':'LastName'}"));
    }

}
