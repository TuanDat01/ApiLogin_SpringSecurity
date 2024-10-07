package com.example.CRUD_Exception.Responsitory;

import com.example.CRUD_Exception.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleResponsitory extends JpaRepository<Role,String> {
}
