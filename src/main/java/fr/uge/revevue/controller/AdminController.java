package fr.uge.revevue.controller;

import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.form.PasswordForm;
import fr.uge.revevue.form.SignupForm;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AdminController {

    private final UserService userService;
    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model){

        var user = userService.currentUser();
        if (user != null){
            model.addAttribute("auth", UserInformation.from(user));
        }

        var users = userService.getAllUser();
        model.addAttribute("users", users);

        return "/admin";
    }

}
