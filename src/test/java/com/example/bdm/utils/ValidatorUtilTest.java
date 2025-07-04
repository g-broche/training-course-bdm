package com.example.bdm.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bdm.dto.RequestRegister;

@SpringBootTest
class ValidatorUtilTest {
    @Autowired
    ValidatorUtil validatorUtil;

    @Test
    void testIsValidFirstName() {
        String tooLongName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertFalse(validatorUtil.isValidFirstName("a"), "Should return false if too short");
        assertFalse(validatorUtil.isValidFirstName(tooLongName), "Should return false if too long");
        assertTrue(validatorUtil.isValidFirstName("valid"), "Should return true if valid");
    }

    @Test
    void testIsValidLastName() {
        String tooLongName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertFalse(validatorUtil.isValidLastName("a"), "Should return false if too short");
        assertFalse(validatorUtil.isValidLastName(tooLongName), "Should return false if too long");
        assertTrue(validatorUtil.isValidLastName("valid"), "Should return true if valid");
    }

    @Test
    void testIsValidEmail() {
        String invalidEmail = "valid@test.t";
        String validEmail = "valid@test.test";
        assertFalse(validatorUtil.isValidEmail(invalidEmail), "Should return false if wrong format");
        assertTrue(validatorUtil.isValidEmail(validEmail), "Should return true if valid");
    }

    @Test
    void testIsValidPassword() {
        String invalidPassword = "123";
        String validPassword = "12345678";
        assertFalse(validatorUtil.isValidPassword(invalidPassword), "Should return false if too short");
        assertTrue(validatorUtil.isValidPassword(validPassword), "Should return true valid");
    }

    @Test
    void testValidateRegisterInputs_GivenValidInputs_ReturnsNoError() {
        RequestRegister inputs = new RequestRegister(
                "John",
                "Doe",
                "john.d@test.test",
                "12345678"
        );
        List<String> errors = validatorUtil.validateRegisterInputs(inputs);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testValidateRegisterInputs_GivenAllInvalidInputs_ReturnsAllErrors() {
        RequestRegister inputs = new RequestRegister(
                "J",
                "D",
                "john.d@test.t",
                "1234567"
        );

        List<String> expectedError = List.of(
                "first name must have between 3 and 30 characters",
                "last name must have between 3 and 30 characters",
                "email format is invalid",
                "password must have at least 8 characters"
                );

        List<String> errors = validatorUtil.validateRegisterInputs(inputs);

        assertEquals(expectedError.get(0), errors.get(0));
        assertEquals(expectedError.get(1), errors.get(1));
        assertEquals(expectedError.get(2), errors.get(2));
        assertEquals(expectedError.get(3), errors.get(3));
    }
}