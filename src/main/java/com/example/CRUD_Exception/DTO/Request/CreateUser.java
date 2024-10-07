package com.example.CRUD_Exception.DTO.Request;

import com.example.CRUD_Exception.Validation.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUser {
    String userName;
    @Size(min = 8,message = "USER_VALID")
    String password;
    String firstName;
    String lastName;
    @DobConstraint(min = 25,message = "NOENOUGHAGE")
    LocalDate dob;
    List<String> roles;


}
