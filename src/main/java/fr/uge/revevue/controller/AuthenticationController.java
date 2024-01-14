package fr.uge.revevue.controller;

import fr.uge.revevue.entity.User;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
@SessionAttributes("user")
public class AuthenticationController {

    @Autowired
    private UserService service;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/signup")
    public String signupForm(User user, Model model){
        return "signup";
    }

    @PostMapping("/signup")
    public String signupForm(@ModelAttribute("user") @Valid User user,
                           BindingResult result,
                           Model model){
        if (result.hasErrors()){
            return "signup";
        }
        service.signup(user);
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(User user, Model model){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("user") @Valid User user,
                                         BindingResult result,
                                         Model model){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(Model model, SessionStatus sessionStatus){
        sessionStatus.setComplete();
        return "redirect:/";
    }

}
