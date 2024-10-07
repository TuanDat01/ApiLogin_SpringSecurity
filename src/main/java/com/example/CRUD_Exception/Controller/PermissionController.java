package com.example.CRUD_Exception.Controller;

import com.example.CRUD_Exception.DTO.Request.PermissionRq;
import com.example.CRUD_Exception.DTO.Response.ApiResponse;
import com.example.CRUD_Exception.DTO.Response.PermissionRp;
import com.example.CRUD_Exception.Service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    ApiResponse<PermissionRp> Create(@RequestBody PermissionRq permissionRq){
        return ApiResponse.<PermissionRp>builder()
                .result(permissionService.create(permissionRq))
                .build();
    }

    @GetMapping("/list")
    ApiResponse<List<PermissionRp>> FindAll(){
        List<PermissionRp> permissionRps = permissionService.FindAll();
        return ApiResponse.<List<PermissionRp>>builder()
                .result(permissionRps)
                .build();
    }

    @DeleteMapping("/{name}")
    void delete(@PathVariable String name){
        permissionService.DeletePermission(name);
    }

}
