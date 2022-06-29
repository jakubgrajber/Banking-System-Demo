package com.smart.tech.start.rest;

import com.smart.tech.start.registration.registration.RegistrationRequest;
import com.smart.tech.start.registration.user.User;
import com.smart.tech.start.registration.user.UserRepository;
import com.smart.tech.start.registration.user.UserRole;
import com.smart.tech.start.registration.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
public class RegistrationControllerTest {

    private static final String FIRSTNAME = "James";
    private static final String LASTNAME = "Hetfield";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "j.h@example.com";
    private static final String INVALID_MAIL = "example.com";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    JacksonTester<RegistrationRequest> registrationRequestTester;

    @Test
    @DisplayName("POST api/registration - register new user")
    public void register_validUser_success() throws Exception {

        // given
        RegistrationRequest request = new RegistrationRequest(FIRSTNAME, LASTNAME, PASSWORD, EMAIL);

        // when - then
        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationRequestTester.write(request).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST api/registration - register new user without request body returns 400")
    public void register_RequestBodyMissing_ClientError() throws Exception {

        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/registration")).andReturn();
        int status =mvcResult.getResponse().getStatus();

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @DisplayName("GET api/registration/confirmation?token= - provide user with verification link")
    public void confirm_validToken_success() throws Exception {

        //given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        String token = userService.signUpUser(user);

        // when - then
        mockMvc.perform(get("/api/registration/confirmation?token=" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(token))
                .andExpect(status().isOk());
    }
}
