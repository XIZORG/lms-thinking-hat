package com.lms.hat.thinking.service;

import com.lms.hat.thinking.model.user.RegistrationRequest;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.model.user.UserResponse;
import com.lms.hat.thinking.model.user.UserUpdateRequest;

public interface UserService {
    UserResponse saveUser(RegistrationRequest registrationRequest);
    UserEntity findByLogin(String login);
    UserEntity findByLoginAndPassword(String login, String password);
    void subscribeCourse(Long courseId, String userName);
    UserResponse findById(Long id);
    void deleteUser(Long id);
    UserResponse editUser(Long id, UserUpdateRequest userUpdateRequest);
}
