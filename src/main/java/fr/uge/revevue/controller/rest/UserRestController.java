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
    private final UserService userService;
    @Autowired
    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") @Valid long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
