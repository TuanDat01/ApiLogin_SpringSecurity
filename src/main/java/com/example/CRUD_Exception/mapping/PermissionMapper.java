package com.example.CRUD_Exception.mapping;

import com.example.CRUD_Exception.DTO.Request.CreateUser;
import com.example.CRUD_Exception.DTO.Request.PermissionRq;
import com.example.CRUD_Exception.DTO.Request.UpdateUser;
import com.example.CRUD_Exception.DTO.Response.PermissionRp;
import com.example.CRUD_Exception.DTO.Response.UserResponse;
import com.example.CRUD_Exception.Entity.Permission;
import com.example.CRUD_Exception.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PermissionMapper {
    Permission toPermission(PermissionRq permissionRq);
    @Mapping(source = "des",target = "description")
    PermissionRp toResponse(Permission permission);
}
