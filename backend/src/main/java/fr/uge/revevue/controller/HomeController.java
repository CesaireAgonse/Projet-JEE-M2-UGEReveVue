package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.CodeInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {
    private final static int LIMIT = 3;
    UserService userService;
    CodeService codeService;

    @Autowired
    public HomeController(CodeService codeService, UserService userService){
        this.userService = userService;
        this.codeService = codeService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public String homePage(@RequestParam(value = "sortBy", required = false, defaultValue = "")String sortBy,
                           @RequestParam(value = "q", required = false, defaultValue = "")String query,
                           @RequestParam(value = "pageNumber", required = false)Integer pageNumber,
                           Model model) {
        var user = userService.currentUser();
        if (user != null){
            model.addAttribute("auth", SimpleUserInformation.from(user));
        }
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        List<CodeInformation> codes;

        switch (sortBy != null ? sortBy : "") {
            // Display all codes by newest
            case "newest" -> {
                codes = codeService.findWithKeywordByNewest(query, pageNumber, CodeService.LIMIT);
            }
            // Display all codes by relevance
            case "relevance"-> {
                codes = codeService.findWithKeywordByScore(query, pageNumber, CodeService.LIMIT);
            }
            default -> {
                if(user != null) {
                    // Display codes from follows
                    codes = codeService.getCodeFromFollowed(user, query, pageNumber, CodeService.LIMIT);
                }
                else {
                    // Display all codes
                    codes = codeService.findWithKeyword(query, pageNumber, CodeService.LIMIT);
                }
            }
        }

        model.addAttribute("codes", codes);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("q", query);
        model.addAttribute("pageNumber", pageNumber);
        return "home";
    }
}
