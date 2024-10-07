package com.example.CRUD_Exception.Service;

import com.example.CRUD_Exception.DTO.Request.RoleRq;
import com.example.CRUD_Exception.DTO.Response.RoleRp;
import com.example.CRUD_Exception.Entity.Role;
import com.example.CRUD_Exception.Responsitory.PermissionResponsitory;
import com.example.CRUD_Exception.Responsitory.RoleResponsitory;
import com.example.CRUD_Exception.mapping.RoleMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoleService {
    RoleResponsitory roleResponsitory;
    PermissionResponsitory permissionResponsitory;
    RoleMapper roleMapper;
    public RoleRp Create(RoleRq request){
        Role role = roleMapper.toRole(request);
        var permission = permissionResponsitory.findAllById(request.getPermissionSet());
        role.setPermissions(new HashSet<>(permission));
        return roleMapper.toRoleRp(roleResponsitory.save(role));
    }

    public List<RoleRp> FindAll(){
        List<Role> roles = roleResponsitory.findAll();
        return roles.stream().map(item -> roleMapper.toRoleRp(item)).toList();
    }

    public void delete(String name){
        roleResponsitory.deleteById(name);
    }
}
