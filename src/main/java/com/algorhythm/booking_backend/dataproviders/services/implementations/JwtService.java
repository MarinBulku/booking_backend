package com.algorhythm.booking_backend.dataproviders.services.implementations;

import com.algorhythm.booking_backend.core.entities.RevokedToken;
import com.algorhythm.booking_backend.dataproviders.repositories.RevokedTokensRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    /*
    * JWT SERVICE
    * Builds and verifies tokens
    * RevokedTokensRepository to check which tokens are revoked before
    * */
    private static final String SECRET_KEY = "56692738757545333c46492d227b747e5d495c2b4941764e59675b2f63";
    private final RevokedTokensRepository revokedTokensRepository;

    /*
    * extractUserEmail(String token)
    * token - User token, to retrieve UserDetails
    * */
    public String extractUserEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /*
    * isTokenValid(String token, UserDetails userDetails)
    * token - User token
    * userDetails - Details of user
    *
    * The method retrieves user's username from the token,
    * compares it if user's username is equal with the one retrieved
    * from token, if the token isn't expired and if the token isn't revoked before
    *
    * If all conditions above are true, the method returns true,
    * otherwise returns false
    * */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserEmail(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenRevoked(token));
    }

    /*
    * isTokenRevoked(String token)
    * token - token to be checked
    *
    * Method tries to retrieve a token from repository with that id.
    *
    * If a token is found, method returns true, else returns false
    * */
    private boolean isTokenRevoked(String token) {
        Optional<RevokedToken> optional = revokedTokensRepository.findById(token);
        return optional.isPresent();
    }

    /*
    * boolean isTokenExpired(String token)
    * token - token to be checked if it is expired
    *
    * Expiration of token is extracted, and if it is before now, returns true
    * else false
    * */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /*
    * extractExpiration(String token)
    *
    * token - Token to retrieve expiration date
    *
    * returns the date of expiration of token
    * */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
    * generateToken(UserDetails userDetails)
    *
    * userDetails - User Details to use to generate a token
    *
    * returns a string as a token
    * */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    /*
    * generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    * extraClaims - claims of the token name : value
    * userDetails - Details of user to generate a token
    *
    * Returns a string token encoded with the secret key
    * */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .claim("Role", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /*
    * extractAllClaims(String token)
    *
    * For a given token, returns all the claims of it
    * */
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*
    * getSigningKey() - No parameters
    *
    * returns a Key object that contain the secret key
    * */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    * revokeToken(String token)
    * token - Token to be revoked
    *
    * If the token is already revoked, returns false
    * Otherwise, stores the token in the repository and returns true
    * */
    public boolean revokeToken(String token){

        if (revokedTokensRepository.existsById(token))
            return false;

        RevokedToken newRevokedToken = RevokedToken.builder()
                .revokedToken(token)
                .timeRevoked(LocalDateTime.now())
                .build();

        revokedTokensRepository.save(newRevokedToken);

        return revokedTokensRepository.existsById(token);
    }
}
