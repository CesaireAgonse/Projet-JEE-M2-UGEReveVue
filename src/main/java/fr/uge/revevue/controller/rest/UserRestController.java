package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.AuthenticationInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<UserInformation> signup(@RequestBody @Valid AuthenticationInformation authenticationInformation){
        userService.signup(authenticationInformation.username(), authenticationInformation.password());
        return ResponseEntity.ok(userService.getInformation(authenticationInformation.username()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserInformation> login(@RequestBody @Valid AuthenticationInformation authenticationInformation){
        return ResponseEntity.ok(userService.getInformation(authenticationInformation.username()));
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{codeId}")
    public ResponseEntity<Void> userDeleted(@PathVariable("codeId") @Valid long codeId) {
        userService.delete(codeId);
        return ResponseEntity.noContent().build();
    }
}
