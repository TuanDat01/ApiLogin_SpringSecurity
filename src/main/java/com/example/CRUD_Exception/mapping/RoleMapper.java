package com.example.CRUD_Exception.mapping;

import com.example.CRUD_Exception.DTO.Request.PermissionRq;
import com.example.CRUD_Exception.DTO.Request.RoleRq;
import com.example.CRUD_Exception.DTO.Response.PermissionRp;
import com.example.CRUD_Exception.DTO.Response.RoleRp;
import com.example.CRUD_Exception.Entity.Permission;
import com.example.CRUD_Exception.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoleMapper {
    @Mapping(target = "permissions",ignore = true)
    Role toRole(RoleRq roleRq);
    RoleRp toRoleRp(Role role);
}
