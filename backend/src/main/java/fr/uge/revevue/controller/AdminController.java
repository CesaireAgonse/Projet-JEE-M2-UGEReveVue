package fr.uge.revevue.controller;

import fr.uge.revevue.information.user.AuthInformation;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.ReviewService;
import fr.uge.revevue.service.UserService;
import fr.uge.revevue.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class AdminController {

    private final static int LIMIT = 5;
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPage(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                            Model model){
        var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user is null");
        }
        model.addAttribute("auth", AuthInformation.from(user));
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var userInfoPage = userService.getSomeUsersForAdminPage(pageNumber, LIMIT);
        model.addAttribute("userPageInformation", userInfoPage);
        return "/admin";
    }
}
