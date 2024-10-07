package com.example.CRUD_Exception.ApplicationInitConfig;

import com.example.CRUD_Exception.ApplicationInitConfig.CustomeJwtDecode;
import com.example.CRUD_Exception.ApplicationInitConfig.JwtAuthenticationEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINT = {"/auth/log-in","/auth/introspect","/users","auth/logout","auth/refresh"};
    private final String[] PRIVATE_ENDPOINT = {"/role/*","permission/*"};
    @Autowired
    CustomeJwtDecode customeJwtDecode;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.POST,PRIVATE_ENDPOINT).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")
                        .anyRequest().authenticated());
        http.oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer -> jwtConfigurer.decoder(customeJwtDecode)
                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntry()));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();

    }
    @Value("${security}")
    private String security_key;
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
