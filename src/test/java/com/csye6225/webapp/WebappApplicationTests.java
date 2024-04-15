package com.csye6225.webapp;

import com.csye6225.webapp.dto.request.CreateUserRequestDto;
import com.csye6225.webapp.dto.request.UpdateUserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebappApplicationTests {

    @Autowired
    private ObjectMapper mapper;

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @PostConstruct
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    void createGetSuccessTest() throws Exception {
        CreateUserRequestDto user = new CreateUserRequestDto("pranav0715@gmail.com", "Pranav", "Prakash", "pas");
        String json = mapper.writeValueAsString(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("pranav0715@gmail.com", "pas");
        ValidatableResponse postResponse = given()
                .contentType(ContentType.JSON).body(json)
                .log().all()
                .when()
                .post("/v8/user")
                .then()
                .log().all().assertThat().statusCode(201);
        String userid = postResponse.extract().path("id");
        given()
                .param("username", "pranav0715@gmail.com")
                .param("token", userid)
                .header("isIntegrationTest", "true")
                .log().all()
                .when()
                .get("/v8/user/verify")
                .then()
                .log().all().assertThat().statusCode(200);
        given()
                .headers(headers)
                .log().all()
                .when()
                .get("/v8/user/self")
                .then()
                .log().all().assertThat().statusCode(200)
                .body("username", equalTo("pranav0715@gmail.com"))
                .body("first_name", equalTo("Pranav"))
                .body("last_name", equalTo("Prakash"));
    }

    @Test
    @Order(2)
    void updateGetSuccessTest() throws Exception {
        UpdateUserRequestDto user = new UpdateUserRequestDto("FirstName", "LastName", "password");
        String json = mapper.writeValueAsString(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("pranav0715@gmail.com", "pas");
        given()
                .headers(headers)
                .contentType(ContentType.JSON).body(json)
                .log().all()
                .when()
                .put("/v8/user/self")
                .then()
                .log().all().assertThat().statusCode(204);

        headers.setBasicAuth("pranav0715@gmail.com", "password");
        given()
                .headers(headers)
                .log().all()
                .when()
                .get("/v8/user/self")
                .then()
                .log().all().assertThat().statusCode(200)
                .body("username", equalTo("pranav0715@gmail.com"))
                .body("first_name", equalTo("FirstName"))
                .body("last_name", equalTo("LastName"));
    }

}
