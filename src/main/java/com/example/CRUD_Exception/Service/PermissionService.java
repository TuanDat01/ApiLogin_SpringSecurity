package com.example.CRUD_Exception.Service;

import com.example.CRUD_Exception.DTO.Request.PermissionRq;
import com.example.CRUD_Exception.DTO.Response.PermissionRp;
import com.example.CRUD_Exception.Entity.Permission;
import com.example.CRUD_Exception.Responsitory.PermissionResponsitory;
import com.example.CRUD_Exception.mapping.PermissionMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {
    PermissionResponsitory permissionResponsitory;
    PermissionMapper permissionMapper;
    @PreAuthorize("hasRole('ADMIN')")
    public PermissionRp create(PermissionRq request){
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toResponse(permissionResponsitory.save(permission));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<PermissionRp> FindAll(){
        List<Permission> permissions = permissionResponsitory.findAll();
        return permissions.stream().map(item->permissionMapper.toResponse(item)).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void DeletePermission(String name){
        Permission permission = permissionResponsitory.findById(name).orElseThrow(() -> new RuntimeException("no permission"));
        permissionResponsitory.delete(permission);
    }
}
