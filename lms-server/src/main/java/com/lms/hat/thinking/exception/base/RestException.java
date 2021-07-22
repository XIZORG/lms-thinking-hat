package com.lms.hat.thinking.exception.base;

import com.lms.hat.thinking.exception.model.FieldErrorModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RestException extends RuntimeException {

    private List<FieldErrorModel> fieldErrors;

    public RestException(List<FieldErrorModel> fieldErrors){
        this.fieldErrors = fieldErrors;
    }
}
