package start.rest;

import com.smart.tech.start.UserApplication;
import com.smart.tech.start.registration.email.EmailValidator;
import com.smart.tech.start.registration.registration.RegistrationRequest;
import com.smart.tech.start.registration.registration.RegistrationService;
import com.smart.tech.start.registration.token.ConfirmationTokenService;
import com.smart.tech.start.registration.user.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UserApplication.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
public class RegistrationControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JacksonTester<RegistrationRequest> registrationRequestTester;

    @Mock
    RegistrationService registrationService;
    @Mock
    EmailValidator emailValidator;
    @Mock
    UserService userService;
    @Mock
    ConfirmationTokenService confirmationTokenService;
    @Mock
    KafkaTemplate kafkaTemplate;

    AutoCloseable autoCloseable;

    private static final String FIRSTNAME = "James";
    private static final String LASTNAME = "Hetfield";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "j.h@example.com";

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        registrationService = new RegistrationService(emailValidator, userService, confirmationTokenService, kafkaTemplate);

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("POST api/registration - register new user")
    //@Disabled
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
        int status = mvcResult.getResponse().getStatus();

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }
}
