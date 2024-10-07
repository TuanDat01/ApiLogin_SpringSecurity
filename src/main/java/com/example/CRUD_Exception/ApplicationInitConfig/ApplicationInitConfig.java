package com.example.CRUD_Exception.ApplicationInitConfig;

import com.example.CRUD_Exception.Entity.Permission;
import com.example.CRUD_Exception.Entity.Role;
import com.example.CRUD_Exception.Entity.User;
import com.example.CRUD_Exception.Responsitory.PermissionResponsitory;
import com.example.CRUD_Exception.Responsitory.RoleResponsitory;
import com.example.CRUD_Exception.Responsitory.UserResponsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserResponsitory userResponsitory, RoleResponsitory roleResponsitory, PermissionResponsitory permissionResponsitory){
        return args -> {
            if (userResponsitory.findByUserName("admin").isEmpty()){
                Set<Permission> permissions = new HashSet<>();
                Set<Role> roles = new HashSet<>();
                Permission permission = Permission.builder().name("ALL").des("ALL_PERMISSION").build();
                permissions.add(permission);
                Role role = Role.builder().permissions(permissions).name("ADMIN").des("ALL").build();
                roles.add(role);
                User user = User.builder()
                        .userName("admin")
                        .roles(roles)
                        .password(bCryptPasswordEncoder.encode("12345678"))
                        .build();
                permissionResponsitory.save(permission);
                roleResponsitory.save(role);
                userResponsitory.save(user);
                log.warn(user.toString());
            }
        };
    }
}
