package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.AuthenticationInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.security.JwtService;
import fr.uge.revevue.security.RefreshToken;
import fr.uge.revevue.service.AuthenticationService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("api/v1")
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationRestController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody @Valid AuthenticationInformation authenticationInformation){
        authenticationService.signup(authenticationInformation.username(), authenticationInformation.password());
        return authenticationService.login(authenticationInformation.username(), authenticationInformation.password());
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid AuthenticationInformation authenticationInformation){
        return authenticationService.login(authenticationInformation.username(), authenticationInformation.password());
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody @Valid RefreshToken token){
        System.out.println(token);
        return authenticationService.refresh(Map.of("refresh", token.refresh()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}
