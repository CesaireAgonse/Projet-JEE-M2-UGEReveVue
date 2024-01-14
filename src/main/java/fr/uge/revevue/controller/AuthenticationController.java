package fr.uge.revevue.controller;

import fr.uge.revevue.entity.User;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@SessionAttributes("user")
public class AuthenticationController {
    private final UserService userService;
    @Autowired
    public AuthenticationController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupGet(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute("user") @Valid User user,
                           BindingResult result){
        if (result.hasErrors()){
            return "signup";
        }
        userService.signup(user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginGet(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("user") @Valid User user,
                                         BindingResult result){
        if (result.hasErrors()){
            return "login";
        }
        return "redirect:/";
    }
}
