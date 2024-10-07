package com.example.CRUD_Exception.ApplicationInitConfig;

import com.example.CRUD_Exception.DTO.Request.IntrospectRequest;
import com.example.CRUD_Exception.Responsitory.InvalidTokenResponsitory;
import com.example.CRUD_Exception.Service.AuthService;
import com.example.CRUD_Exception.exception.AppException;
import com.example.CRUD_Exception.exception.ErrorCode;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;
@Component
public class CustomeJwtDecode implements JwtDecoder {
    @Value("${security}")
    protected String SIGNER_KEY;
    NimbusJwtDecoder nimbusJwtDecoder = null;
    @Autowired
    AuthService authService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var check = authService.checkKey(IntrospectRequest.builder().tokenKey(token).build());
            if (!check.isValid())
                throw new JwtException("token is valid");
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);

    }
}
