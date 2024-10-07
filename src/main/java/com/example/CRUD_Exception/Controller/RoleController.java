package com.example.CRUD_Exception.Controller;

import com.example.CRUD_Exception.DTO.Request.PermissionRq;
import com.example.CRUD_Exception.DTO.Request.RoleRq;
import com.example.CRUD_Exception.DTO.Response.ApiResponse;
import com.example.CRUD_Exception.DTO.Response.PermissionRp;
import com.example.CRUD_Exception.DTO.Response.RoleRp;
import com.example.CRUD_Exception.Service.PermissionService;
import com.example.CRUD_Exception.Service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    ApiResponse<RoleRp> Create(@RequestBody RoleRq roleRq){
        RoleRp roleRp = roleService.Create(roleRq);
        System.out.println(roleRp.getDes()+roleRp.getName()+roleRp.getPermissions());
        return ApiResponse.<RoleRp>builder()
                .result(roleRp)
                .build();
    }

    @GetMapping("/list")
    ApiResponse<List<RoleRp>> FindAll(){
        List<RoleRp> roleRps = roleService.FindAll();
        return ApiResponse.<List<RoleRp>>builder()
                .result(roleRps)
                .build();
    }

    @DeleteMapping("/{name}")
    void delete(@PathVariable String name){
        roleService.delete(name);
    }

}
