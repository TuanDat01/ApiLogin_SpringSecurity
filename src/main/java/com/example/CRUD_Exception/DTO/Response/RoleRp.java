package com.example.CRUD_Exception.DTO.Response;

import com.example.CRUD_Exception.Entity.Permission;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRp {
    String name;
    String des;
    Set<Permission> permissions;
}
