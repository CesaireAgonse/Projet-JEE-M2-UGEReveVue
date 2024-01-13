package fr.uge.revevue.controller;

import fr.uge.revevue.entity.User;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/signup")
    public String signupForm(User user, Model model){
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute("user") @Valid User user,
                             BindingResult result,
                             Model model){
        if (result.hasErrors()){
            return "signup";
        }
        service.signup(user);
        model.addAttribute("user", user);
        return "home-session";
    }

    @GetMapping("/login")
    public String loginForm(User user, Model model){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("user") @Valid User user,
                            BindingResult result,
                            Model model){
        if (result.hasErrors()){
            return "login";
        }
        var auth = service.find(user);
        if (auth.isPresent()) {
            model.addAttribute("user", auth);
            return "home-session";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, SessionStatus sessionStatus){
        sessionStatus.setComplete();
        return "redirect:/login";
    }
}
