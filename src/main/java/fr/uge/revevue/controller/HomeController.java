package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
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
    private final static int LIMIT = 20;
    UserService userService;
    CodeService codeService;

    @Autowired
    public HomeController(CodeService codeService, UserService userService){
        this.userService = userService;
        this.codeService = codeService;
    }

    @GetMapping("/")
    public String homePage(@RequestParam(value = "q", required = false)String query, Model model) {
        model.addAttribute("auth", userService.currentUser());
        List<Code> codes = List.of();
        System.out.println(query);
        if(query == null) {
            codes = codeService.findAll(0, LIMIT);
        }
        else {
            codes = codeService.findByTitleContaining(query);
        }
        model.addAttribute("codes", codes);
        return "home";
    }
}
