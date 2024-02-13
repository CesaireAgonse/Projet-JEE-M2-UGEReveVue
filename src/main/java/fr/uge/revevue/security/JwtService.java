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

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final UserService userService;
    @Value("${encryption.key}")
    private String ENCRYPTION_KEY;
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    @Autowired
    public JwtService(UserService userService){
        this.userService = userService;
    }
    public Map<String, String> generate(String username){
        User user = this.userService.loadUserByUsername(username);
        return this.generateJwt(user);
    }

    public String readUsername(String token) {
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

    private Map<String, String> generateJwt(User user) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + EXPIRATION_TIME;
        var claims = Map.of(
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getUsername()
        );
        final var bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(user.getUsername())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer);
    }

    private Key getKey() {
        var decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);


    }

//    public Map<String, String> refreshToken(Map<String, String> token, String authorizationToken) {
//        if (!token.get("bearer").equals(authorizationToken)){
//            throw new IllegalStateException("Token");
//        }
//        if (isTokenExpired(authorizationToken)){
//            throw new RuntimeException();
//        }
//        return generate(readUsername(authorizationToken));
//    }
}
