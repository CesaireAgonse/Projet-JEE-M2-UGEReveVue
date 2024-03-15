package fr.uge.revevue.controller;

import fr.uge.revevue.security.CookieService;
import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.service.AuthenticationService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Controller
@PreAuthorize("permitAll()")
public class AuthenticationController {
    private final UserService userService;
    private final CookieService cookieService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(UserService userService, CookieService cookieService, AuthenticationService authenticationService){
        this.userService = userService;
        this.cookieService = cookieService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute("signupForm") SignupForm signupForm){
        if (userService.currentUser() != null){
            return "redirect:/";
        }
        return "users/signup";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm){
        if (userService.currentUser() != null){
            return "redirect:/";
        }
        return "users/login";
    }

    @GetMapping("/refresh")
    public String refresh(HttpServletRequest request, HttpServletResponse response){
        var cookie = cookieService.findCookie("refresh", request);
        if (cookie == null) {
            return "redirect:/login";
        }
        var tokens = authenticationService.refresh(Map.of(cookie.getName(), cookie.getValue()));
        cookieService.addAllCookiesFromTokens(tokens, response);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
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
        authenticationService.signup(signupForm.getUsername(), signupForm.getPassword());
        var tokens = authenticationService.login(signupForm.getUsername(), signupForm.getPassword());
        cookieService.addAllCookiesFromTokens(tokens, response);
        return "redirect:/";
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
        var tokens = authenticationService.login(loginForm.getUsername(), loginForm.getPassword());
        cookieService.addAllCookiesFromTokens(tokens, response);
        return "redirect:/";
    }
}
