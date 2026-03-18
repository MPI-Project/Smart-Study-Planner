package com.smartstudyplanner.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UserValidator")
class UserValidatorTest {

    private UserValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserValidator();
    }

    @Nested
    @DisplayName("validate email")
    class ValidateEmail {

        @Test
        @DisplayName("acceptă email valid")
        void validEmail_passes() {
            assertDoesNotThrow(() -> validator.validate("user@example.com", "parola123"));
        }

        @Test
        @DisplayName("respinge email fără @")
        void emailWithoutAt_throws() {
            Exception ex = assertThrows(Exception.class,
                    () -> validator.validate("invalidemail", "parola123"));
            assertTrue(ex.getMessage().contains("Email format invalid"));
        }

        @Test
        @DisplayName("respinge email cu format invalid")
        void malformedEmail_throws() {
            assertThrows(Exception.class,
                    () -> validator.validate("@nodomain.com", "parola123"));
            assertThrows(Exception.class,
                    () -> validator.validate("spaces in@email.com", "parola123"));
        }
    }

    @Nested
    @DisplayName("validate password")
    class ValidatePassword {

        @Test
        @DisplayName("acceptă parolă cu min. 8 caractere și cifre")
        void validPassword_passes() {
            assertDoesNotThrow(() -> validator.validate("a@b.com", "parola123"));
            assertDoesNotThrow(() -> validator.validate("a@b.com", "abc12345"));
        }

        @Test
        @DisplayName("respinge parolă prea scurtă")
        void passwordTooShort_throws() {
            Exception ex = assertThrows(Exception.class,
                    () -> validator.validate("a@b.com", "abc1234"));
            assertTrue(ex.getMessage().contains("8 caractere"));
        }

        @Test
        @DisplayName("respinge parolă fără cifre")
        void passwordWithoutDigit_throws() {
            assertThrows(Exception.class,
                    () -> validator.validate("a@b.com", "doarlitere"));
        }

        @Test
        @DisplayName("respinge parolă fără litere mici")
        void passwordWithoutLowercase_throws() {
            assertThrows(Exception.class,
                    () -> validator.validate("a@b.com", "PAROLA123"));
        }
    }
}
