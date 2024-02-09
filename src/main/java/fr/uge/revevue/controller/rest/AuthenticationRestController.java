package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.AuthenticationInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class AuthenticationRestController {
    private final UserService userService;

    @Autowired
    public AuthenticationRestController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SimpleUserInformation> signup(@RequestBody @Valid AuthenticationInformation authenticationInformation,
                                                        BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        var user = userService.signup(authenticationInformation.username(), authenticationInformation.password());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public  ResponseEntity<SimpleUserInformation> login(@RequestBody @Valid AuthenticationInformation authenticationInformation,
                                                        BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        var user = userService.login(authenticationInformation.username(), authenticationInformation.password());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged out successfully");
    }
}
