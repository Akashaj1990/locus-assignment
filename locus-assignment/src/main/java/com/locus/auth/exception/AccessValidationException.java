package com.locus.auth.exception;

import com.locus.auth.enums.ValidationError;

public class AccessValidationException extends Exception {

    private ValidationError error;

    public AccessValidationException(ValidationError error) {
        super(error.msg);
    }

}
