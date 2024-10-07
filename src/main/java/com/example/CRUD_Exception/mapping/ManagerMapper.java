package com.example.CRUD_Exception.mapping;

import com.example.CRUD_Exception.DTO.Response.UserResponse;
import com.example.CRUD_Exception.DTO.Request.CreateUser;
import com.example.CRUD_Exception.DTO.Request.UpdateUser;
import com.example.CRUD_Exception.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ManagerMapper {
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "roles",ignore = true)
    User toUser(CreateUser createUser);
    UserResponse toUserResponse(User createUser);
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "roles",ignore = true)
    void updateUser(@MappingTarget User user, UpdateUser updateUser);
}
