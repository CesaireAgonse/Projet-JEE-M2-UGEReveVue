package fr.uge.revevue.controller.rest;

import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.information.AuthenticationInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
public class UserRestController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    @Autowired
    public UserRestController(AuthenticationManager authenticationManager, UserService userService){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserInformation> signup(@RequestBody @Valid AuthenticationInformation authenticationInformation){
        userService.signup(authenticationInformation.username(), authenticationInformation.password());
        return ResponseEntity.ok(userService.getInformation(authenticationInformation.username()));
    }

    @PostMapping("/login")
    public  ResponseEntity<SimpleUserInformation> login(@RequestBody @Valid AuthenticationInformation authenticationInformation,
                                                                        BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationInformation.username(), authenticationInformation.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(SimpleUserInformation.from(userService.currentUser()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged out successfully");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{codeId}")
    public ResponseEntity<Void> userDeleted(@PathVariable("codeId") @Valid long codeId) {
        userService.delete(codeId);
        return ResponseEntity.noContent().build();
    }
}
