package com.example.CRUD_Exception.exception;

import com.example.CRUD_Exception.DTO.Response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MIN_VALUE = "min";
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingException(AppException runtimeException){
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = runtimeException.getErrorCode();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValid(MethodArgumentNotValidException runtimeException){
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String key = runtimeException.getFieldError().getDefaultMessage();
        Map<String,Object> attribute = null;
        try {
            errorCode = ErrorCode.valueOf(key);
            var constrainviolation = runtimeException.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
            attribute = constrainviolation.getConstraintDescriptor().getAttributes();
            log.warn(attribute.toString());
        }
        catch (IllegalArgumentException e){
            System.out.println("loi kh co enum key");
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(mapValue(errorCode.getMessage(),attribute));
        return ResponseEntity.badRequest().body(apiResponse);
    }
    public String mapValue(String mess, Map<String, Object> attribute){
        String value = String.valueOf(attribute.get(MIN_VALUE));
        return mess.replace("{"+MIN_VALUE+"}",value);
    }
//@ExceptionHandler(value = AccessDeniedException.class)
//    ResponseEntity<ApiResponse> handle(AccessDeniedException accessDeniedException){
//        ErrorCode errorCode = ErrorCode.NOAUTHORZIZED;
//        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
//}
//    @ExceptionHandler(value = IllegalArgumentException.class)
//    ResponseEntity<ApiResponse> handLingBan(IllegalArgumentException illegalArgumentException){
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setMessage(illegalArgumentException.getMessage());
//        apiResponse.setCode(0);
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//@ExceptionHandler(value = Exception.class)
//ResponseEntity<ApiResponse> handleAllException(Exception exception){
//    ErrorCode errorCode = ErrorCode.INVALID_KEY;
//    try {
//        errorCode = ErrorCode.NOENOUGHAGE;
//    }catch (IllegalArgumentException e)
//    {
//        System.out.println("kh ton tai key");
//    }
//    return ResponseEntity.badRequest().body(ApiResponse.builder()
//                    .code(errorCode.getCode())
//                    .message(errorCode.getMessage())
//            .build());
//}
}
