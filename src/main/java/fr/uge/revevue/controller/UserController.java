package fr.uge.revevue.controller;

import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.PasswordForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService){
        this.authenticationManager = authenticationManager;
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
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    @GetMapping("/password")
    public String password(@ModelAttribute("passwordForm") PasswordForm passwordForm, Model model){
        model.addAttribute("auth", SimpleUserInformation.from(userService.currentUser()));
        return "users/password";
    }

    @PostMapping("/password")
    public String password(@ModelAttribute("passwordForm") @Valid PasswordForm passwordForm, BindingResult result, Model model){
        model.addAttribute("auth", SimpleUserInformation.from(userService.currentUser()));
        if (result.hasErrors()){
            return "users/password";
        }
        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())){
            result.rejectValue("confirmPassword", "error.passwordForm", "The confirmation of the new password does not match the new password entered.");
            return "users/password";
        }
        if(userService.matchesPassword(passwordForm.getNewPassword())){
            result.rejectValue("newPassword", "error.passwordForm", "The new password should be different from the current password.");
            return "users/password";
        }
        if (!userService.matchesPassword(passwordForm.getCurrentPassword())) {
            result.rejectValue("currentPassword", "error.passwordForm", "The current password you entered is incorrect. Please try again.");
            return "users/password";
        }
        var userInformation = userService.modifyPassword(passwordForm.getCurrentPassword(), passwordForm.getNewPassword());
        return "redirect:/users/" + userInformation.username();
    }

    @GetMapping("/users/{username}")
    public String information(@PathVariable String username, Model model){
        var userInformation = userService.getInformation(username);
        if (userInformation == null){
            return "redirect:/";
        }
        model.addAttribute("auth", userService.getInformation(userService.currentUser().getUsername()));
        model.addAttribute("user", userInformation);
        return "users/profile";
    }

    @PostMapping("/follow/{username}")
    public String follow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return "redirect:/";
        }
        userService.follow(userService.currentUser().getUsername(), username);
        return "redirect:/users/" + username;
    }

    @PostMapping("/unfollow/{username}")
    public String unfollow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return "redirect:/";
        }
        userService.unfollow(userService.currentUser().getUsername(), username);
        return "redirect:/users/" + username;
    }
}
