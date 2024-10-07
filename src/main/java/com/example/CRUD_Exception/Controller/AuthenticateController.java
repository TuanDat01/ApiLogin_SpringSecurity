package com.example.CRUD_Exception.Controller;

import com.example.CRUD_Exception.DTO.Request.LogoutRequest;
import com.example.CRUD_Exception.DTO.Request.RefreshRequest;
import com.example.CRUD_Exception.DTO.Response.AuthResponse;
import com.example.CRUD_Exception.DTO.Response.IntospectResponse;
import com.example.CRUD_Exception.DTO.Response.ApiResponse;
import com.example.CRUD_Exception.DTO.Request.AuthRequest;
import com.example.CRUD_Exception.DTO.Request.IntrospectRequest;
import com.example.CRUD_Exception.Service.AuthService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateController {
    AuthService authService;
    @PostMapping("/log-in")
    ApiResponse<AuthResponse> Login(@RequestBody AuthRequest authRequest){
        return ApiResponse.<AuthResponse>builder()
                .result(authService.isUser(authRequest))
                .build();
    }
    @PostMapping("/introspect")
    IntospectResponse checkJWT(@RequestBody IntrospectRequest intospectRequest) throws ParseException, JOSEException {
        System.out.println("1");

        return authService.checkKey(intospectRequest);
    }
    @PostMapping("/logout")
    void logoutTok(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        System.out.println("1");
        authService.logoutToken(logoutRequest);
        System.out.println("2");
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) throws ParseException, JOSEException {
        log.warn("---------" + refreshRequest.getTokenKey());
        AuthResponse authResponse = authService.refreshToken(refreshRequest);
        return ApiResponse.<AuthResponse>builder().result(authResponse).build();
    }
}
