package fr.uge.revevue.controller;

import fr.uge.revevue.entity.User;
import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupGet(@ModelAttribute("signupForm") SignupForm signupForm){
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute("signupForm") @Valid SignupForm signupForm,
                             BindingResult result){
        System.out.println(signupForm.getPassword());
        if (result.hasErrors() || !signupForm.getPassword().equals(signupForm.getConfirmPassword())){
            return "signup";
        }
        userService.signup(signupForm.getUsername(), signupForm.getPassword());
        userService.login(signupForm.getUsername(), signupForm.getPassword());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginGet(@ModelAttribute("loginForm") LoginForm loginForm){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("loginForm") @Valid LoginForm loginForm,
                            BindingResult result){
        if (result.hasErrors()){
            return "login";
        }
        userService.login(loginForm.getUsername(), loginForm.getPassword());
        return "redirect:/";
    }
}
