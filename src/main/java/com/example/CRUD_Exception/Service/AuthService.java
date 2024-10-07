package com.example.CRUD_Exception.Service;

import com.example.CRUD_Exception.DTO.Request.LogoutRequest;
import com.example.CRUD_Exception.DTO.Request.RefreshRequest;
import com.example.CRUD_Exception.DTO.Response.AuthResponse;
import com.example.CRUD_Exception.DTO.Response.IntospectResponse;
import com.example.CRUD_Exception.DTO.Request.AuthRequest;
import com.example.CRUD_Exception.DTO.Request.IntrospectRequest;
import com.example.CRUD_Exception.Entity.InvalidToken;
import com.example.CRUD_Exception.Entity.User;
import com.example.CRUD_Exception.Responsitory.InvalidTokenResponsitory;
import com.example.CRUD_Exception.Responsitory.UserResponsitory;
import com.example.CRUD_Exception.exception.AppException;
import com.example.CRUD_Exception.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserResponsitory userResponsitory;

    InvalidTokenResponsitory invalidTokenResponsitory;
    @NonFinal
    @Value("${security}")
    protected String SIGNER_KEY;
    @NonFinal
    @Value("${valid-duration}")
    protected String valid;
    @NonFinal
    @Value("${refreshable-duration}")
    protected String refreshable;
    public AuthResponse isUser(AuthRequest authRequest){
        Optional<User> user = userResponsitory.findByUserName(authRequest.getUsername());
        if(user.isEmpty()) {
            throw new AppException(ErrorCode.NO_USER);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if(passwordEncoder.matches(authRequest.getPass(),user.get().getPassword()) == false) {
            throw new AppException(ErrorCode.NOAUTHENTICATED);
        }
        var key = generateKey(user.get());
        return AuthResponse.builder()
                .result(true)
                .token(key)
                .build();
    }

    public IntospectResponse checkKey(IntrospectRequest introspectRequest) throws JOSEException, ParseException {
        boolean result = true;
        try {
            signedJWT(introspectRequest.getTokenKey(),false);
        }catch (AppException a){
            result = false;
        }
        return IntospectResponse.builder()
                .valid(result)
                .build();

    }
    public SignedJWT signedJWT(String token,boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expire = (isRefresh)
                ?new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(Long.parseLong(refreshable),ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();
        if(!(signedJWT.verify(jwsVerifier) && expire.after(new Date())))
        {
            throw new AppException(ErrorCode.NOAUTHENTICATED);
        }
        if (invalidTokenResponsitory.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.NOAUTHENTICATED);
        }
        return signedJWT;
    }
    public void logoutToken(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            var signedJWT = signedJWT(logoutRequest.getTokenKey(),true);
            InvalidToken invalidToken = new InvalidToken();
            invalidToken.setIdToken(signedJWT.getJWTClaimsSet().getJWTID());
            invalidToken.setExpiredTime(signedJWT.getJWTClaimsSet().getExpirationTime());
            invalidTokenResponsitory.save(invalidToken);
        }catch (AppException e){
            log.warn("token already expired");
        }
    }

    public AuthResponse refreshToken(RefreshRequest refreshRequest) throws ParseException, JOSEException {
        SignedJWT signedJWT = signedJWT(refreshRequest.getTokenKey(),true);
        LogoutRequest logoutRequest = LogoutRequest.builder().tokenKey(refreshRequest.getTokenKey()).build();
        logoutToken(logoutRequest);
        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userResponsitory.findByUserName(username).orElseThrow(()->new AppException(ErrorCode.NO_USER));
        String tokenNew = generateKey(user);
        return AuthResponse.builder().token(tokenNew).result(true).build();
    }
    private String generateKey(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("dat")
                .issueTime(new Date())
                .expirationTime(
                        new Date(Instant.now().plus(Long.parseLong(valid), ChronoUnit.SECONDS).toEpochMilli())
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",getAuth(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create key");
            throw new RuntimeException(e);
        }

    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    private String getAuth(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(item ->
                    {
                        stringJoiner.add("ROLE_"+item.getName());
                        if (!CollectionUtils.isEmpty(item.getPermissions())){
                            item.getPermissions().forEach(permission ->
                                    stringJoiner.add(permission.getName()));
                        }
                    }
            );

        }
        return stringJoiner.toString();
    }
}
