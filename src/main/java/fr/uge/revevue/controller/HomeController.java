package fr.uge.revevue.controller;

import fr.uge.revevue.entity.User;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

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
    public String homePage(Model model) {
        model.addAttribute("auth", userService.currentUser());
        var codes = codeService.findAll();
        model.addAttribute("codes", codes);
        return "home";
    }
}
