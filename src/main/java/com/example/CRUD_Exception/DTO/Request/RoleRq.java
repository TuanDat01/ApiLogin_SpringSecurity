package com.example.CRUD_Exception.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.security.Permission;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRq {
    String name;
    String des;
    Set<String> permissionSet;
}

