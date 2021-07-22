package com.lms.hat.thinking.controller;

import com.google.gson.Gson;
import com.lms.hat.thinking.config.security.services.CustomUserDetails;
import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.model.user.UserResponse;
import com.lms.hat.thinking.model.user.UserUpdateRequest;
import com.lms.hat.thinking.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/courses")
    public ResponseEntity<DefaultResponse> subscribeCourse(@RequestBody Long id,
                                                  Authentication authentication){
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        userService.subscribeCourse(id, cud.getUsername());
        return ResponseEntity.ok(new DefaultResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultResponse> deleteUser(@PathVariable(name = "id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(new DefaultResponse());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable(name = "id") Long id,
                                                 @RequestBody UserUpdateRequest uur){
        return ResponseEntity.ok(userService.editUser(id, uur));
    }
}
