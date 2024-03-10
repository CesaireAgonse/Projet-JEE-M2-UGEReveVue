package fr.uge.revevue.controller;

import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
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
    private final CodeService codeService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @Autowired
    public AdminController(UserService userService, CodeService codeService, ReviewService reviewService, CommentService commentService){
        this.userService = userService;
        this.codeService = codeService;
        this.reviewService = reviewService;
        this.commentService = commentService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminPage(@RequestParam(value = "pageNumber", required = false)Integer pageNumber,
                            Model model){
        var user = userService.currentUser();
        if (user == null){
            throw new IllegalStateException("user is null");
        }
        model.addAttribute("auth", SimpleUserInformation.from(user));

        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        model.addAttribute("pageNumber", pageNumber);

        var usersList = userService.getSomeUsers(pageNumber, LIMIT);
        model.addAttribute("users", usersList);

        var codesByUser = new HashMap<UserInformation, Long>();
        var reviewsByUser = new HashMap<UserInformation, Long>();
        var commentsByUser = new HashMap<UserInformation, Long>();
        for (var userl : usersList){
            var numberOfCodes = codeService.countCodesFromUser(userl);
            var numberOfReviews = reviewService.countReviewsFromUser(userl);
            var numberOfComments = commentService.countCommentsFromUser(userl);
            codesByUser.putIfAbsent(userl, numberOfCodes);
            reviewsByUser.putIfAbsent(userl, numberOfReviews);
            commentsByUser.putIfAbsent(userl, numberOfComments);
        }
        model.addAttribute("codesByUser", codesByUser);
        model.addAttribute("reviewsByUser", reviewsByUser);
        model.addAttribute("commentsByUser", commentsByUser);
        return "/admin";
    }
}
