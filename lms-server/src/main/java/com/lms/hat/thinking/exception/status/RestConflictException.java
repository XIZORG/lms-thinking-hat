package com.lms.hat.thinking.exception.status;

import com.lms.hat.thinking.exception.base.RestException;
import com.lms.hat.thinking.exception.model.FieldErrorModel;

import java.util.List;

public class RestConflictException extends RestException {

    public RestConflictException(List<FieldErrorModel> fieldErrors) {
        super(fieldErrors);
    }
}
