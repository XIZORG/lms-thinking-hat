package com.lms.hat.thinking.exception.model;

import com.lms.hat.thinking.exception.base.RestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseModel {

    private List<FieldErrorModel> fieldErrors;

    public ErrorResponseModel(RestException e) {
        this.fieldErrors = e.getFieldErrors();
    }
}
