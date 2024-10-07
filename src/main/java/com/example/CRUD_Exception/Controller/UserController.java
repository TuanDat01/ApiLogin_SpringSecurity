package com.example.CRUD_Exception.Controller;

import com.example.CRUD_Exception.DTO.Response.UserResponse;
import com.example.CRUD_Exception.DTO.Response.ApiResponse;
import com.example.CRUD_Exception.DTO.Request.CreateUser;
import com.example.CRUD_Exception.DTO.Request.UpdateUser;
import com.example.CRUD_Exception.Entity.User;
import com.example.CRUD_Exception.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody @Valid CreateUser createUser){
        ApiResponse<UserResponse> userResponse = new ApiResponse<>();
        userResponse.setResult(userService.Create(createUser));
        return userResponse;
    }

    @GetMapping
    public List<User> getList(){
        return userService.FindAllUser();
    }
    @GetMapping("/info")
    public ApiResponse<UserResponse> Info(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.Info())
                .build();
    }

    @GetMapping("/{userId}")
    public User FindUser(@PathVariable String userId){
        return userService.FindUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable String userId, @RequestBody @Valid UpdateUser updateUser){
        userService.UpdateUser(updateUser,userId);
    }
}
