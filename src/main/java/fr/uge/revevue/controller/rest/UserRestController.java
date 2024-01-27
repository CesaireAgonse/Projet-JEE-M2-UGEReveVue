package fr.uge.revevue.controller.rest;

import fr.uge.revevue.dto.UserAuthenticationDTO;
import fr.uge.revevue.dto.UserAuthenticationDTO;
import fr.uge.revevue.dto.UserInformationDTO;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<UserInformationDTO> signup(@RequestBody @Valid UserAuthenticationDTO userDTO){
        var user = userService.signup(userDTO.getUsername(), userDTO.getPassword());
        return ResponseEntity.ok(new UserInformationDTO(userDTO.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserInformationDTO> login(@RequestBody @Valid UserAuthenticationDTO userDTO){

        return (userDTO != null) ? ResponseEntity.ok(new UserInformationDTO(userDTO.getUsername())) : ResponseEntity.status(405).build();
    }




}
