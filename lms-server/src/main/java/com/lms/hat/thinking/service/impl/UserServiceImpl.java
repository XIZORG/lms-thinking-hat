package com.lms.hat.thinking.service.impl;

import com.lms.hat.thinking.exception.model.FieldErrorModel;
import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.user.*;
import com.lms.hat.thinking.repository.CourseRepository;
import com.lms.hat.thinking.repository.RefreshTokenRepository;
import com.lms.hat.thinking.repository.RoleEntityRepository;
import com.lms.hat.thinking.repository.UserEntityRepository;
import com.lms.hat.thinking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    private final UserEntityRepository userEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public UserServiceImpl(UserEntityRepository userEntityRepository,
                           RoleEntityRepository roleEntityRepository,
                           CourseRepository courseRepository,
                           PasswordEncoder passwordEncoder,
                           RefreshTokenRepository refreshTokenRepository) {
        this.userEntityRepository = userEntityRepository;
        this.roleEntityRepository = roleEntityRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public UserResponse saveUser(RegistrationRequest registrationRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(registrationRequest.getPassword());
        userEntity.setLogin(registrationRequest.getLogin());
        userEntity.setEmail(registrationRequest.getEmail());

        RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
        userEntity.setRoleEntity(userRole);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        try {
            userEntityRepository.save(userEntity);
        } catch (Exception ex) {
            logger.error("Can`t create user with login {}", userEntity.getLogin());
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("user", HttpStatus.CONFLICT.getReasonPhrase(),
                            "login or email already exist")));
        }

        logger.info("User with login {} was successfully created", userEntity.getLogin());
        return UserResponse.responseUserFromEntity(userEntity);
    }

    @Override
    public UserEntity findByLogin(String login) {
        UserEntity user = userEntityRepository.findByLogin(login);
        if (user == null) {
            logger.warn("Can`t find user with login {}", login);
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("login", HttpStatus.CONFLICT.getReasonPhrase(),
                            "user not found")));
        }
        return user;
    }

    @Override
    public UserEntity findByLoginAndPassword(String login, String password) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
            logger.warn("Invalid login attempt, login: {}", login);
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("password", HttpStatus.CONFLICT.getReasonPhrase(),
                            "bad password")));
        }

        throw new RestConflictException(Collections.singletonList(
                new FieldErrorModel("login", HttpStatus.CONFLICT.getReasonPhrase(),
                        "user not found")));
    }

    @Override
    @Transactional
    public void subscribeCourse(Long courseId, String userName) {
        Course course = courseRepository.findById(courseId).get();
        UserEntity user = userEntityRepository.findByLogin(userName);
        user.addCourse(course);
        userEntityRepository.save(user);
    }

    @Transactional
    @Override
    public UserResponse findById(Long id) {
        UserEntity user =  userEntityRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));
        return UserResponse.responseUserFromEntity(user);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user =  userEntityRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));
        refreshTokenRepository.deleteByUser(user);
        userEntityRepository.delete(user);
    }

    @Override
    public UserResponse editUser(Long id, UserUpdateRequest uur) {
        UserEntity user =  userEntityRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));
        user.setEmail(uur.getEmail());
        user.setAge(uur.getAge());
        user.setFullName(uur.getFullName());
        user.setPhone(uur.getPhone());
        return UserResponse.responseUserFromEntity(userEntityRepository.save(user));
    }
}
