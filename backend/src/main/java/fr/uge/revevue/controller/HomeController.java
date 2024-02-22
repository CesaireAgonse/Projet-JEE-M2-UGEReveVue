package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.information.UserInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    UserService userService;
    CodeService codeService;

    @Autowired
    public HomeController(CodeService codeService, UserService userService){
        this.userService = userService;
        this.codeService = codeService;
    }

    @GetMapping("/")
    public String homePage(@RequestParam(value = "q", required = false, defaultValue = "")String query,
                           @RequestParam(value = "pageNumber", required = false)Integer pageNumber,
                           Model model) {
        var user = userService.currentUser();
        if (user != null){
            model.addAttribute("auth", SimpleUserInformation.from(user));
        }
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        var codes = codeService.findWithKeyword(query, pageNumber, CodeService.LIMIT);
        model.addAttribute("codes", codes);
        model.addAttribute("pageNumber", pageNumber);
        return "home";
    }
}
