package com.lms.hat.thinking.exception.status;

import com.lms.hat.thinking.exception.base.RestException;
import com.lms.hat.thinking.exception.model.FieldErrorModel;

import java.util.List;

public class RestUnauthorizedException extends RestException {

    public RestUnauthorizedException(List<FieldErrorModel> fieldErrors) {
        super(fieldErrors);
    }
}
