package com.example.CRUD_Exception.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXSIST(10021,"trung ten userr", HttpStatus.BAD_REQUEST),
    USER_VALID(1003,"it nhat {min} chu so", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1002,"key not exist",HttpStatus.BAD_REQUEST),
    NOAUTHENTICATED(1006,"no AUTHENTICATED", HttpStatus.UNAUTHORIZED),
    NO_USER(10001,"user is not found",HttpStatus.NOT_FOUND),
    NOAUTHORZIZED(1007,"you do not permission",HttpStatus.FORBIDDEN),
    NOENOUGHAGE(1008,"you do not enough age {min}",HttpStatus.BAD_REQUEST)


    ;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
    private HttpStatusCode httpStatusCode;
    private int code;
    private String message;

}
