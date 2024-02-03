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
                         BindingResult result,
                         Model model){
        if (result.hasErrors() || !signupForm.getPassword().equals(signupForm.getConfirmPassword())){
            if (!signupForm.getPassword().equals(signupForm.getConfirmPassword())){
                result.rejectValue("password", "error.signupForm", "The password is not the same.");
            }
            return "users/signup";
        }
        try {
            userService.signup(signupForm.getUsername(), signupForm.getPassword());
        } catch (IllegalArgumentException e){
            if (e.getMessage().equals("username already used")){
                result.rejectValue("username", "error.signupForm", "This user name is already taken.");
            }
            return "users/signup";
        }

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
    public String password(@ModelAttribute("passwordForm") PasswordForm passwordForm, Model model){
        model.addAttribute("auth", userService.getInformation(userService.currentUser().getUsername()));
        return "users/password";
    }

    @PostMapping("/password")
    public String password(@ModelAttribute("passwordForm") @Valid PasswordForm passwordForm, BindingResult result){
        if (result.hasErrors() || !passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())){
            return "users/password";
        }
        if(passwordForm.getNewPassword().equals(passwordForm.getCurrentPassword())){
            return "users/password";
        }
        userService.modifyPassword(userService.currentUser().getUsername(), passwordForm.getNewPassword(), passwordForm.getCurrentPassword());
        return "redirect:/users/" + userService.currentUser().getUsername();
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
        var userInformation= userService.getInformation(username);
        if (userInformation == null){
            return "redirect:/";
        }
        var user = userService.currentUser();
        userService.follow(user.getUsername(), username);
        return "redirect:/users/" + username;
    }

    @PostMapping("/unfollow/{username}")
    public String unfollow(@PathVariable String username){
        var userInformation = userService.getInformation(username);
        if (userInformation == null){
            return "redirect:/";
        }
        var user = userService.currentUser();
        userService.unfollow(user.getUsername(), username);
        return "redirect:/users/" + username;
    }

}
