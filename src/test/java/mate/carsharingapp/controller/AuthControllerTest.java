package mate.carsharingapp.controller;

import static mate.carsharingapp.util.TestUtil.createResponseAfterRegistration;
import static mate.carsharingapp.util.TestUtil.createUserRegistrationRequestDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.carsharingapp.dto.user.UserLoginRequestDto;
import mate.carsharingapp.dto.user.UserLoginResponseDto;
import mate.carsharingapp.dto.user.UserRegistrationRequestDto;
import mate.carsharingapp.dto.user.UserResponseDto;
import mate.carsharingapp.exception.RegistrationException;
import mate.carsharingapp.service.security.AuthenticationService;
import mate.carsharingapp.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserServiceImpl userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Register with valid request returns 200 and user DTO")
    void registration_WithValidRequest_Ok() throws Exception {
        UserRegistrationRequestDto requestDto = createUserRegistrationRequestDto();
        UserResponseDto responseDto = createResponseAfterRegistration();

        when(userService.register(any(UserRegistrationRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/registration")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

    }

    @Test
    @DisplayName("Register user with email already exist should throw RegistrationException")
    void registrationUser_WhenEmailAlreadyExist_NotOK() throws Exception {
        UserRegistrationRequestDto requestDto = createUserRegistrationRequestDto();
        when(userService.register(any(UserRegistrationRequestDto.class)))
                .thenThrow(new RegistrationException("User with email"
                        + requestDto.getEmail() + "already exist"));
        mockMvc.perform(post("/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    @DisplayName("Login user")
    void loginUser_WithValidRequest_Ok() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("12345678");
        UserLoginResponseDto responseDto = new UserLoginResponseDto("testJwtToken");
        when(authenticationService.authenticate(any(UserLoginRequestDto.class)))
                .thenReturn(responseDto);
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testJwtToken"));
    }
}
