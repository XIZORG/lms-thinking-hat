package com.lms.hat.thinking.exception.status;


import com.lms.hat.thinking.exception.base.RestException;
import com.lms.hat.thinking.exception.model.FieldErrorModel;

import java.util.List;

public class RestBadRequestException extends RestException {

    public RestBadRequestException(List<FieldErrorModel> fieldErrors) {
        super(fieldErrors);
    }
}
