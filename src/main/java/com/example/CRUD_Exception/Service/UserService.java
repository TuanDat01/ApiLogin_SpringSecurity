package com.example.CRUD_Exception.Service;

import com.example.CRUD_Exception.DTO.Response.UserResponse;
import com.example.CRUD_Exception.DTO.Request.CreateUser;
import com.example.CRUD_Exception.DTO.Request.UpdateUser;
import com.example.CRUD_Exception.Entity.User;
import com.example.CRUD_Exception.Responsitory.RoleResponsitory;
import com.example.CRUD_Exception.Responsitory.UserResponsitory;
import com.example.CRUD_Exception.exception.AppException;
import com.example.CRUD_Exception.exception.ErrorCode;
import com.example.CRUD_Exception.mapping.ManagerMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserService {
    UserResponsitory userResponsitory;
    ManagerMapper managerMapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    PasswordEncoder passwordEncoder;

    RoleResponsitory roleResponsitory;
    public UserResponse Create(CreateUser requestUser){
        if(userResponsitory.existsByUserName(requestUser.getUserName()))
            throw new AppException(ErrorCode.USER_EXSIST);
        User user = managerMapper.toUser(requestUser);
        user.setPassword(bCryptPasswordEncoder.encode(requestUser.getPassword()));
        var roles = roleResponsitory.findAllById(Collections.singleton("USER"));
        user.setRoles(new HashSet<>(roles));
        UserResponse userResponse = managerMapper.toUserResponse(userResponsitory.save(user));
        return managerMapper.toUserResponse(userResponsitory.save(user));
    }
    public List<User> FindAllUser(){
        var authen = SecurityContextHolder.getContext().getAuthentication();
        return userResponsitory.findAll();
    }
    @PostAuthorize("returnObject.userName == authentication.name")
    public User FindUserById(String id){
        return userResponsitory.findById(id).orElseThrow(()->new RuntimeException("NO_USER"));
    }

    public void deleteUser(String id){
        userResponsitory.deleteById(id);
    }

    public boolean sameUser(String user){
        List<User> userList = userResponsitory.findAll();
        for (User x:
             userList) {
            if (x.getUserName()==user)
                return false;

        }
        return true;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public User UpdateUser(UpdateUser updateUser,String id){
        User user = FindUserById(id);
        log.warn("++++");
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        log.warn(updateUser.getPassword());
        if (!Objects.isNull(updateUser.getRoles()))
        {
            try {
                var roles = roleResponsitory.findAllById(updateUser.getRoles());
                System.out.println(roles+"-------------");

            }
            catch (HttpMessageNotReadableException e){
                System.out.println("loi " +e.toString() );
            }
            var roles = roleResponsitory.findAllById(updateUser.getRoles());
            user.setRoles(new HashSet<>(roles));
        }
        managerMapper.updateUser(user,updateUser);
        return userResponsitory.save(user);
    }

    public UserResponse Info(){
        var authen = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userResponsitory.findByUserName(authen.getName());
        return managerMapper.toUserResponse(userResponsitory.findByUserName(authen.getName()).orElseThrow(() -> new RuntimeException("NO_USER")));
    }
}
