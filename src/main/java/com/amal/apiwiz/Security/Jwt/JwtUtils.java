package com.amal.apiwiz.Security.Jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;


/**
 * Utility class for handling JWT token generation, validation, and extraction.
 */
@Component
public class JwtUtils {
    //logger instance
    private static final Logger logger= LoggerFactory.getLogger(JwtUtils.class);

    // Retrieves JWT secret,JWT expiration time from application.properties
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token for the provided user details.
     *
     * @param userDetails - UserDetails object representing the user
     * @return - JWT token string
     */
    public String generateJwtTokenForUser(UserDetails userDetails){
        // Builds a JWT token with user details and signs it using the JWT secret key
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Retrieves the username from the provided JWT token.
     *
     * @param token - JWT token string
     * @return - Username extracted from the token
     */
    public String getUserName(String token){
        return getClaims(token, Claims::getSubject);
    }

    /**
     * Validates the provided JWT token against the provided UserDetails.
     *
     * @param token - JWT token string
     * @param userDetails - UserDetails object representing the user
     * @return - Boolean indicating whether the token is valid for the user
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=getUserName(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }
    /**
     * Retrieves the signing key for JWT token generation.
     *
     * @return - Key object representing the signing key
     */
    private Key getSignKey() {
        // Decodes the JWT secret key and generates the signing key
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts the claims from the provided JWT token using the given function.
     *
     * @param token - JWT token string
     * @param claimsResolver - Function to resolve the claims from the parsed JWT token
     * @return - Result obtained by applying the claims resolver function
     */
    private <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        // Parses the JWT token and extracts the claims using the provided function
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the provided JWT token and retrieves all the claims.
     *
     * @param token - JWT token string
     * @return - Claims extracted from the JWT token
     */
    private Claims getAllClaims(String token) {
        // Parses the JWT token and retrieves all the claims
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token - JWT token string
     * @return - Boolean indicating whether the token has expired
     */
    private boolean isTokenExpired(String token) {
        // Extracts the expiration time from the token claims and compares it with the current time
        return getClaims(token, Claims::getExpiration).before(new Date());
    }

}