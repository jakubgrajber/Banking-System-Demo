package com.smart.tech.start.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtUtil {

    private static final String secretKey = "SecretSecretSecretSecretSecretSecretSecretSecretSecretSecretSecret";

    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private static final Algorithm algorithm = Algorithm.HMAC256(getSecretKeyInBytes());

    public static String getAuthorizationHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }

    public static String extractSubject(HttpServletRequest servletRequest) {
        String header = getAuthorizationHeader(servletRequest);
        String token = removeHeaderPrefix(header);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public static String extractSubject(String authorizationHeader) {
        String jwt = removeHeaderPrefix(authorizationHeader);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getSubject();
    }

    public static String generateAccessToken(String subject, String issuer, List<String> authorities) {

        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles", authorities)
                .sign(algorithm);
    }

    public static String generateRefreshToken(String subject, String issuer) {

        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public static boolean isPrefixGood(String authorizationHeader) {
        return authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX);
    }

    public static String removeHeaderPrefix(String authorizationHeader) {
        return authorizationHeader.substring(AUTHORIZATION_HEADER_PREFIX.length());
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static byte[] getSecretKeyInBytes() {
        return secretKey.getBytes();
    }

    public static Algorithm getAlgorithm(){
        return algorithm;
    }

}
