package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.AuthenticationInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.security.JwtService;
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
    private final UserService userService;
    private AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Autowired
    public AuthenticationRestController(UserService userService, JwtService jwtService){
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
    @GetMapping("/test")
    public ResponseEntity<String> test(){
            return ResponseEntity.ok("SALUT");
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid AuthenticationInformation authenticationInformation,
                                     BindingResult result){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationInformation.username(), authenticationInformation.password())
        );
        if (authentication.isAuthenticated()){
             return this.jwtService.generate(authenticationInformation.username());
        }
        return null;
    }

//    @PostMapping("/refresh")
//    public @ResponseBody Map<String, String> refresh(@RequestBody Map<String, String> token, HttpServletRequest request){
//        String authorization = request.getHeader("Authorization");
//        if(authorization == null || !authorization.startsWith("Bearer ")){
//            return null;
//        }
//        return this.jwtService.refreshToken(token, authorization.substring(7));
//    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}
