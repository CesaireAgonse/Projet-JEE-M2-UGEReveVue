package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.AuthenticationInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.security.JwtService;
import fr.uge.revevue.service.AuthenticationService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/v1")
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;
    @Autowired
    public AuthenticationRestController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SimpleUserInformation> signup(@RequestBody @Valid AuthenticationInformation authenticationInformation,
                                                        BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        var user = authenticationService.signup(authenticationInformation.username(), authenticationInformation.password());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid AuthenticationInformation authenticationInformation){
        return authenticationService.login(authenticationInformation.username(), authenticationInformation.password());
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> token){
        return authenticationService.refresh(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}
