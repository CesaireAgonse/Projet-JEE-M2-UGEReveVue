package fr.uge.revevue.security;

import fr.uge.revevue.entity.User;
import fr.uge.revevue.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${token.encryption.key}")
    private String ENCRYPTION_KEY;
    @Value("${token.expiration.time.authentication}")
    private long TOKEN_EXPIRATION_TIME;
    @Value("${token.expiration.time.refresh}")
    private long TOKEN_EXPIRATION_REFRESH_TIME;

    private final UserService userService;

    @Autowired
    public JwtService(UserService userService){
        this.userService = userService;
    }

    public Map<String, String> generate(String username){
        User user = this.userService.loadUserByUsername(username);
        return this.generateTokenWithRefreshToken(user);
    }

    public String getUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        try{
            Date expirationDate = getExpirationDateFromToken(token);
            return expirationDate.before(new Date());
        }catch (ExpiredJwtException e){
            return true;
        }
    }

    public Map<String, String> refreshToken(Map<String, String> token) {
        var refresh = token.get("refresh");
        if (isTokenExpired(refresh)){
            throw new RuntimeException("token invalide");
        }
        return generate(getUsername(refresh));
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String createToken(User user, long expirationTime){
        var claims = Map.of(
                Claims.EXPIRATION, new Date(System.currentTimeMillis() + expirationTime),
                Claims.SUBJECT, user.getUsername()
        );
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setSubject(user.getUsername())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, String> generateTokenWithRefreshToken(User user) {
        return Map.of(
                "bearer", createToken(user, TOKEN_EXPIRATION_TIME),
                "refresh", createToken(user, TOKEN_EXPIRATION_REFRESH_TIME)
        );
    }

    private Key getKey() {
        var decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }
}
