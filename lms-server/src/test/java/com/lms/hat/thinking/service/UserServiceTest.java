package com.lms.hat.thinking.service;

import com.lms.hat.thinking.model.user.RegistrationRequest;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.model.user.UserResponse;
import com.lms.hat.thinking.model.user.UserUpdateRequest;
import com.lms.hat.thinking.repository.CourseRepository;
import com.lms.hat.thinking.repository.RefreshTokenRepository;
import com.lms.hat.thinking.repository.RoleEntityRepository;
import com.lms.hat.thinking.repository.UserEntityRepository;
import com.lms.hat.thinking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserEntityRepository userEntityRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        CourseRepository courseRepository = mock(CourseRepository.class);
        RoleEntityRepository roleEntityRepository = mock(RoleEntityRepository.class);
        userEntityRepository = mock(UserEntityRepository.class);

        userService = new UserServiceImpl(userEntityRepository,
                roleEntityRepository,
                courseRepository,
                passwordEncoder,
                refreshTokenRepository);
    }

    @Test
    void testSaveUser() {
        UserEntity user = new UserEntity();
        user.setEmail("some@gmail.com");
        user.setPassword("password");
        user.setLogin("login");

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(user.getEmail());
        registrationRequest.setLogin(user.getLogin());
        registrationRequest.setPassword(user.getPassword());

        when(passwordEncoder.encode(notNull())).thenAnswer(invocation -> {
            String newPass = invocation.getArgument(0);
            newPass = "new String.hashCode()";
            return newPass;
        });

        UserResponse response = userService.saveUser(registrationRequest);
        assertNotNull(response);
        assertThat(response.getLogin().equals(user.getLogin()));
    }

    @Test
    void testFindByLogin(){
        String presentLogin = "test1";
        String unPresentLogin = "test2";

        when(userEntityRepository.findByLogin(presentLogin)).thenAnswer(invocation -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setLogin(presentLogin);
            userEntity.setPassword("some pass");
            userEntity.setEmail("email@gmail.com");
            return userEntity;
        });
        when(userEntityRepository.findByLogin(unPresentLogin)).thenReturn(null);

        UserEntity presentUser = userService.findByLogin(presentLogin);

        assertThat(presentUser.getLogin().equals(presentLogin));
        assertNotNull(presentUser.getPassword());
        assertNotNull(presentUser.getLogin());

        assertThrows(Exception.class,() -> userService.findByLogin(unPresentLogin));
    }

    @Test
    void testFindByLoginAndPassword(){
        String presentLogin = "test1";
        String presentPassword = "test1";
        String unPresentLogin = "test2";
        String unPresentPassword = "test2";

        when(userEntityRepository.findByLogin(unPresentLogin)).thenReturn(null);
        when(userEntityRepository.findByLogin(presentLogin)).thenAnswer(invocation -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setLogin(presentLogin);
            userEntity.setPassword(presentPassword);
            userEntity.setEmail("email@gmail.com");
            return userEntity;
        });
        when(passwordEncoder.matches(presentPassword, presentPassword)).thenReturn(true);
        when(passwordEncoder.matches(presentPassword, unPresentPassword)).thenReturn(false);

        UserEntity presentUser = userService.findByLoginAndPassword(presentLogin,presentPassword);
        assertThat(presentUser.getLogin().equals(presentLogin));
        assertNotNull(presentUser.getPassword());
        assertNotNull(presentUser.getLogin());

        assertThrows(Exception.class,() -> userService.findByLoginAndPassword(unPresentLogin,unPresentPassword));
        assertThrows(Exception.class,() -> userService.findByLoginAndPassword(presentLogin,unPresentPassword));
    }

    @Test
    void TestFindById(){
        Long presentId = 1l;
        Long unPresentId = 2l;

        when(userEntityRepository.findById(presentId)).thenAnswer(invocation -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(presentId);
            userEntity.setLogin("presentLogin");
            userEntity.setPassword("presentPassword");
            userEntity.setEmail("email@gmail.com");
            return Optional.of(userEntity);
        });
        when(userEntityRepository.findById(unPresentId)).thenReturn(Optional.empty());

        UserResponse presentUser = userService.findById(presentId);
        assertThat(presentUser.getId().equals(presentId));
        assertThat(presentUser.getLogin().equals("presentLogin"));
        assertNotNull(presentUser.getLogin());

        assertThrows(Exception.class,() -> userService.findById(unPresentId));
    }

    @Test
    void testDeleteById(){
        Long presentId = 1l;
        Long unPresentId = 2l;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(presentId);
        userEntity.setLogin("presentLogin");
        userEntity.setPassword("presentPassword");
        userEntity.setEmail("email@gmail.com");

        when(userEntityRepository.findById(presentId)).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.findById(unPresentId)).thenReturn(Optional.empty());

        userService.deleteUser(presentId);
        verify(userEntityRepository).findById(presentId);
        verify(userEntityRepository).delete(userEntity);

        assertThrows(Exception.class,() -> userService.deleteUser(unPresentId));

    }

    @Test
    void testEditUser(){
        UserUpdateRequest uur = new UserUpdateRequest();
        uur.setAge(32);
        uur.setEmail("email");
        uur.setFullName("full name");
        uur.setPhone("0999999999");

        Long presentId = 1L;
        Long unPresentId = 2L;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(presentId);
        userEntity.setLogin("presentLogin");
        userEntity.setPassword("presentPassword");

        when(userEntityRepository.findById(presentId)).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.findById(unPresentId)).thenReturn(Optional.empty());
        when(userEntityRepository.save(userEntity)).thenReturn(userEntity);

        assertThrows(Exception.class,() -> userService.editUser(unPresentId, uur));

        UserResponse userResponse = userService.editUser(presentId, uur);

        assertThat(userResponse.getLogin().equals("presentLogin"));
        assertThat(userResponse.getEmail().equals("email"));
        assertThat(userResponse.getFullName().equals("full name"));
        assertThat(userResponse.getPhone().equals("0999999999"));

    }
}
