package fr.uge.revevue.controller.rest;

import fr.uge.revevue.dto.UserAuthenticationDTO;
import fr.uge.revevue.dto.UserAuthenticationDTO;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid UserAuthenticationDTO userDTO){
        User user = userService.signup(userDTO.username(), userDTO.password());
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.status(405).build();
    }

}
