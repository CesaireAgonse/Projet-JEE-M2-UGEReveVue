package fr.uge.revevue.controller;

import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.ReviewService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;

@Controller
public class AdminController {

    private final UserService userService;
    private final CodeService codeService;
    private final ReviewService reviewService;

    @Autowired
    public AdminController(UserService userService, CodeService codeService, ReviewService reviewService){
        this.userService = userService;
        this.codeService = codeService;
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPage(Model model){
        var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user is null");
        }
        model.addAttribute("auth", SimpleUserInformation.from(user));

        /*
        var codesMap = codeService.getAllCodeFromUsers();
        model.addAttribute("codesMap", codesMap);
        var reviewsMap = reviewService.getAllReviewFromUsers();
        model.addAttribute("reviewsMap", reviewsMap);
        */


        var usersList = userService.getAllUser();
        //usersSet.addAll(codesMap.keySet());
        //usersSet.addAll(reviewsMap.keySet());
        model.addAttribute("users", usersList);
        return "/admin";
    }
}
