package com.example.CRUD_Exception.Responsitory;

import com.example.CRUD_Exception.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionResponsitory extends JpaRepository<Permission,String> {

}
