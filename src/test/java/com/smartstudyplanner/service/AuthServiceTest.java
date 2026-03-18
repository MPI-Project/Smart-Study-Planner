package com.smartstudyplanner.service;

import com.smartstudyplanner.entity.User;
import com.smartstudyplanner.repository.UserRepository;
import com.smartstudyplanner.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user;
    private static final String RAW_PASSWORD = "parola123";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "validator", new UserValidator());
        ReflectionTestUtils.setField(authService, "passwordEncoder", new BCryptPasswordEncoder());
        user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword(RAW_PASSWORD);
    }

    @Test
    @DisplayName("signUp validează, criptează parola și salvează user")
    void signUp_validUser_savesEncoded() throws Exception {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.signUp(user);

        verify(userRepository).save(any(User.class));
        assertNotEquals(RAW_PASSWORD, saved.getPassword());
        assertTrue(saved.getPassword().startsWith("$2a$"));
    }

    @Test
    @DisplayName("signUp aruncă dacă email există deja")
    void signUp_duplicateEmail_throws() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(Exception.class, () -> authService.signUp(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("logIn returnează user când parola match-uiește")
    void logIn_validCredentials_returnsUser() throws Exception {
        String encoded = new BCryptPasswordEncoder().encode(RAW_PASSWORD);
        User dbUser = new User();
        dbUser.setEmail(user.getEmail());
        dbUser.setPassword(encoded);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(dbUser));

        User result = authService.logIn(user.getEmail(), RAW_PASSWORD);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("logIn aruncă dacă user nu există")
    void logIn_userNotFound_throws() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> authService.logIn("missing@example.com", RAW_PASSWORD));
    }

    @Test
    @DisplayName("logIn aruncă dacă parola e greșită")
    void logIn_wrongPassword_throws() {
        User dbUser = new User();
        dbUser.setEmail(user.getEmail());
        dbUser.setPassword(new BCryptPasswordEncoder().encode("otherPassword"));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(dbUser));

        assertThrows(Exception.class, () -> authService.logIn(user.getEmail(), RAW_PASSWORD));
    }
}
