package com.example.CRUD_Exception.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role {
    @Id
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String des;

    @ManyToMany
    Set<Permission> permissions;
}