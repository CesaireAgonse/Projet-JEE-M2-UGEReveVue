package fr.uge.revevue.controller;

import fr.uge.revevue.information.CodeInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final CodeService codeService;

    @Autowired
    public AdminController(UserService userService, CodeService codeService){
        this.userService = userService;
        this.codeService = codeService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPage(Model model){
        var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user is null");
        }
        model.addAttribute("auth", SimpleUserInformation.from(user));
        var usersMap = codeService.getAllCodeFromUsers();
        model.addAttribute("usersMap", usersMap);
        return "/admin";
    }
}
