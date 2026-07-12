package com.chocksaway.healthcare.testutils;

import jakarta.validation.Validator;
import jakarta.validation.Validation;

public class ValidationTestFixture {
    public static Validator createValidator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}

