package com.lms.hat.thinking.model.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private Integer age;
    private String email;
    private String phone;
}
