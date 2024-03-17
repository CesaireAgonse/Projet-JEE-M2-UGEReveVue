package fr.uge.revevue.controller.rest;

import fr.uge.revevue.form.AuthenticationForm;
import fr.uge.revevue.security.RefreshToken;
import fr.uge.revevue.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody @Valid AuthenticationForm authenticationInformation, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        authenticationService.signup(authenticationInformation.getUsername(), authenticationInformation.getPassword());
        return ResponseEntity.ok(authenticationService.login(authenticationInformation.getUsername(), authenticationInformation.getPassword()));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid AuthenticationForm authenticationInformation, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authenticationService.login(authenticationInformation.getUsername(), authenticationInformation.getPassword()));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody @Valid RefreshToken token, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authenticationService.refresh(Map.of("refresh", token.refresh())));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
