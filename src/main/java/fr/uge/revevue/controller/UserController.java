package fr.uge.revevue.controller;

import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.PasswordForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return "redirect:/";
    }

    @GetMapping("/password")
    public String password(@ModelAttribute("passwordForm") PasswordForm passwordForm){
        return "users/password";
    }

    @PostMapping("/password")
    public String password(@ModelAttribute("passwordForm") PasswordForm passwordForm,BindingResult result){
        if (result.hasErrors() || !passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())){
            return "redirect:/password";
        }
        if(passwordForm.getNewPassword().equals(passwordForm.getCurrentPassword())){
            return "redirect:/password";
        }
        userService.modifPassword(userService.currentUser().getUsername(),passwordForm.getNewPassword(), passwordForm.getCurrentPassword());
        return "redirect:/users/" + userService.currentUser().getUsername();
    }

    @GetMapping("/users/{username}")
    public String informations(@PathVariable String username, Model model){
        var userInformationDTO = userService.getInformations(username);
        if (userInformationDTO == null){
            return "redirect:/";
        }
        model.addAttribute("auth",userService.getInformations(userService.currentUser().getUsername()));
        model.addAttribute("user", userInformationDTO);
        return "users/profile";
    }

    @PostMapping("/follow/{username}")
    public String follow(@PathVariable String username){
        var userInformationDTO = userService.getInformations(username);
        if (userInformationDTO == null){
            return "redirect:/";
        }
        var user = userService.currentUser();
        userService.follow(user.getUsername(), username);
        return "redirect:/users/" + username;
    }

}
