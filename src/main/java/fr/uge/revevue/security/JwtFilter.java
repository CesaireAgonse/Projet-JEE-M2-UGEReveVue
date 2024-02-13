package fr.uge.revevue.security;

import fr.uge.revevue.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public JwtFilter(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    private Cookie findCookie(String name, Cookie[] cookies){
        if (cookies != null){
            for (var cookie:cookies){
                if (cookie.getName().equals(name)){
                    return cookie;
                }
            }
        }
        return null;
    }

    private void removeCookie(String name, HttpServletResponse response){
        var removeCookie = new Cookie(name, null);
        removeCookie.setPath("/");
        removeCookie.setMaxAge(0);
        response.addCookie(removeCookie);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        String username = null;
        boolean isTokenExpired = true;
        Cookie cookie = findCookie("bearer", request.getCookies());
        if (cookie != null){
            token = cookie.getValue();
            isTokenExpired = jwtService.isTokenExpired(token);
            if (isTokenExpired){
                removeCookie("bearer", response);
                response.sendRedirect("/login");
                return;
            }
            username = jwtService.readUsername(token);
        }
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
            isTokenExpired = jwtService.isTokenExpired(token);
            username = jwtService.readUsername(token);
        }
        if (!isTokenExpired && username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
