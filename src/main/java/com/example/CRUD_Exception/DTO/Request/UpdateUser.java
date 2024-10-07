package com.example.CRUD_Exception.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUser {
    @Size(min = 8,message = "")
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    List<String> roles;
}
