package fr.uge.revevue.security;

import fr.uge.revevue.service.UserService;
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
public class TokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Autowired
    public TokenFilter(UserService userService, JwtService jwtService, CookieService cookieService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        String username = null;
        boolean isTokenExpired = true;
        // Client léger
        Cookie cookie = cookieService.findCookie("bearer", request);
        if (cookie != null){    // Si il y a un cookie d'authentification
            token = cookie.getValue();
            isTokenExpired = jwtService.isTokenExpired(token);
            if (isTokenExpired){    // Si le token dans le cookie est expiré
                cookieService.removeCookie("bearer", response);
                response.sendRedirect("/refresh");  // on fait un refresh token si on peut
                return;
            }
            username = jwtService.getUsername(token);
        }
        // Client lourd
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")){   // Si il y a un jwt d'authentification
            token = authorization.substring(7);
            isTokenExpired = jwtService.isTokenExpired(token);
            username = jwtService.getUsername(token);
        }
        // Chargement de l'utilisateur avec le token
        if (!isTokenExpired && username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
