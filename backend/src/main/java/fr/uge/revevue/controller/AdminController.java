package fr.uge.revevue.controller;

import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        if (user == null){
            throw new IllegalStateException("user is null");
        }
        model.addAttribute("auth", SimpleUserInformation.from(user));
        var users = userService.getAllUser();
        model.addAttribute("users", users);
        return "/admin";
    }
}
