package com.smartstudyplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartstudyplanner.dto.LoginRequest;
import com.smartstudyplanner.entity.User;
import com.smartstudyplanner.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.smartstudyplanner.repository.UserRepository;
import com.smartstudyplanner.validation.UserValidator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@Import({ AuthController.class, AuthControllerTest.StubAuthServiceConfig.class })
@DisplayName("AuthController API")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthControllerTest.StubAuthService stubAuthService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void resetStub() {
        stubAuthService.reset();
    }

    @Test
    @DisplayName("POST /api/auth/login - 200 când credențiale valide")
    void login_validCredentials_returnsOk() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("parola123");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName("Test");
        user.setLastName("User");

        stubAuthService.setLogInResult(user);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    @DisplayName("POST /api/auth/login - 401 când credențiale invalide")
    void login_invalidCredentials_returnsUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("wrong");

        stubAuthService.setLogInException(new Exception("Parolă incorectă!"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/signup - 200 când user nou valid")
    void signup_validUser_returnsOk() throws Exception {
        User user = new User();
        user.setEmail("new@example.com");
        user.setFirstName("New");
        user.setLastName("User");
        user.setPassword("parola123");

        stubAuthService.setSignUpResult(user);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    @DisplayName("POST /api/auth/signup - 400 când service aruncă (ex: email duplicat)")
    void signup_duplicateEmail_returnsBadRequest() throws Exception {
        User user = new User();
        user.setEmail("existing@example.com");
        user.setFirstName("A");
        user.setLastName("B");
        user.setPassword("parola123");

        stubAuthService.setSignUpException(new Exception("Utilizatorul există deja!"));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    /** Stub pentru AuthService (fără Mockito, compatibil Java 25). */
    public static class StubAuthService extends AuthService {
        private User signUpResult;
        private Exception signUpException;
        private User logInResult;
        private Exception logInException;

        void reset() {
            signUpResult = null;
            signUpException = null;
            logInResult = null;
            logInException = null;
        }

        void setSignUpResult(User user) { this.signUpResult = user; }
        void setSignUpException(Exception e) { this.signUpException = e; }
        void setLogInResult(User user) { this.logInResult = user; }
        void setLogInException(Exception e) { this.logInException = e; }

        @Override
        public User signUp(User user) throws Exception {
            if (signUpException != null) throw signUpException;
            return signUpResult != null ? signUpResult : user;
        }

        @Override
        public User logIn(String email, String password) throws Exception {
            if (logInException != null) throw logInException;
            return logInResult;
        }
    }

    @Configuration
    static class StubAuthServiceConfig {
        @Bean
        AuthService authService() {
            return new StubAuthService();
        }

        @Bean
        UserValidator userValidator() {
            return new UserValidator();
        }

        @Bean
        BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}
