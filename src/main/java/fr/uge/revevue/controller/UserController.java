package fr.uge.revevue.controller;

import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.PasswordForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.UUID;

@Controller
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute("signupForm") SignupForm signupForm){
        return "users/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm,
                             BindingResult result){
        if (result.hasErrors() || !signupForm.getPassword().equals(signupForm.getConfirmPassword())){
            return "users/signup";
        }
        userService.signup(signupForm.getUsername(), signupForm.getPassword());
        return "redirect:/";
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
        return "redirect:/";
    }

    @GetMapping("/users/{username}")
    public String informations(@PathVariable String username, Model model){
        var user = userService.getInformations(username);
        if (user == null){
            return "redirect:/";
        }
        model.addAttribute("auth", userService.currentUser());
        model.addAttribute("user", user);
        return "users/information";
    }

    @GetMapping("/password")
    public String password(@ModelAttribute("passwordForm") PasswordForm passwordForm){
        return "users/password";
    }
}
