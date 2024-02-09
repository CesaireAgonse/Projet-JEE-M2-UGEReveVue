package fr.uge.revevue.controller;

import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
@Controller
public class AuthenticationController {
    private final UserService userService;
    @Autowired
    public AuthenticationController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute("signupForm") SignupForm signupForm){
        return "users/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result){
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
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm){
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") @Valid LoginForm loginForm,
                        BindingResult result){
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
        userService.login(loginForm.getUsername(), loginForm.getPassword());
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}
