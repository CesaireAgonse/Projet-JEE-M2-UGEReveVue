package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.AuthenticationInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
public class UserRestController {

    private final UserService userService;
    @Autowired
    public UserRestController(UserService userService, AuthenticationManager authenticationManager){
        this.userService = userService;

    }
    @PostMapping("/signup")
    public ResponseEntity.BodyBuilder signup(@RequestBody @Valid AuthenticationInformation authenticationInformation){
        userService.signup(authenticationInformation.username(), authenticationInformation.password());
        return ResponseEntity.ok();
    }


}
