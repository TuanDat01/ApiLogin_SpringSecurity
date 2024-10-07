package com.example.CRUD_Exception.DTO.Response;

import com.example.CRUD_Exception.Entity.Role;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userName;
    String firstName;
    String lastName;
    LocalDate dob;
    Set<Role> roles;
}
