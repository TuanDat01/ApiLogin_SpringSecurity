package com.example.CRUD_Exception.Responsitory;

import com.example.CRUD_Exception.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserResponsitory extends JpaRepository<User,String> {
    boolean existsByUserName(String username);
    Optional<User> findByUserName(String userName);
}
