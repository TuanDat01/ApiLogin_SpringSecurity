package com.example.CRUD_Exception.Responsitory;

import com.example.CRUD_Exception.Entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenResponsitory extends JpaRepository<InvalidToken,String> {
}
