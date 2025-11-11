package br.com.mattlino.AppProdutos.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.mattlino.AppProdutos.model.User;

import org.springframework.beans.factory.annotation.Value;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateAccessToken(User user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                .withIssuer("AppProdutos")
                .withSubject(user.getLogin())
                .withExpiresAt(genAccessTokenExpirationDate())
                .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String generateRefreshToken(User user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                .withIssuer("AppProdutos")
                .withSubject(user.getLogin())
                .withExpiresAt(genRefreshTokenExpirationDate())
                .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating token", exception);
        }
    }

    public String validateAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("AppProdutos")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    public String validateRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("AppProdutos")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    private Instant genAccessTokenExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant genRefreshTokenExpirationDate() {
        return LocalDateTime.now().plusHours(7).toInstant(ZoneOffset.of("-03:00"));
    }

}
