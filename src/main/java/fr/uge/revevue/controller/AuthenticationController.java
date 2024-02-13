package fr.uge.revevue.controller;

import fr.uge.revevue.security.JwtService;
import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
@Controller
public class AuthenticationController {
    private final UserService userService;
    private AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(UserService userService, JwtService jwtService){
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute("signupForm") SignupForm signupForm){
        if (userService.currentUser() != null){
            return "redirect:/";
        }
        return "users/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result, HttpServletResponse response){
        if (result.hasErrors()){
            return "users/signup";
        }
        if (!signupForm.getPassword().equals(signupForm.getConfirmPassword())){
            result.rejectValue("confirmPassword", "error.signupForm", "Password confirmation does not match the entered password.");
            return "users/signup";
        }
        if (userService.isExisted(signupForm.getUsername())){
            result.rejectValue("username", "error.signupForm", "This username is already taken. Please choose another one.");
            return "users/signup";
        }
        userService.signup(signupForm.getUsername(), signupForm.getPassword());
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupForm.getUsername(), signupForm.getPassword())
        );
        if (authentication.isAuthenticated()){
            var tokens = jwtService.generate(signupForm.getUsername());
            var cookie = new Cookie("bearer", tokens.get("bearer"));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm){
        if (userService.currentUser() != null){
            return "redirect:/";
        }
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") @Valid LoginForm loginForm,
                        BindingResult result, HttpServletResponse response){
        if (result.hasErrors()){
            return "users/login";
        }
        var userOptional = userService.findUserByName(loginForm.getUsername());
        if (userOptional.isEmpty()){
            result.rejectValue("username", "error.loginForm", "The username you entered is incorrect. Please try again.");
            return "users/login";
        }
        var user = userOptional.get();
        if(!userService.matchesPassword(loginForm.getPassword(), user.getPassword())){
            result.rejectValue("password", "error.loginForm", "The password you entered is incorrect. Please try again.");
            return "users/login";
        }
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
        );
        if (authentication.isAuthenticated()){
           var tokens = jwtService.generate(loginForm.getUsername());
           var cookie = new Cookie("bearer", tokens.get("bearer"));
           cookie.setPath("/");
           cookie.setHttpOnly(true);
           response.addCookie(cookie);
        }
        return "redirect:/";
    }

//    @PostMapping("/logout")
//    public String logout(HttpServletResponse response) {
//        SecurityContextHolder.clearContext();
//        Cookie cookieToDelete = new Cookie("bearer", "");
//        cookieToDelete.setMaxAge(0);
//        cookieToDelete.setPath("/");
//        response.addCookie(cookieToDelete);
//        return "redirect:/login";
//    }
}
