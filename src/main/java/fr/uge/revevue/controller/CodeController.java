package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.form.LoginForm;
import fr.uge.revevue.information.CodeInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import fr.uge.revevue.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class CodeController {
    private final UserService userService;
    private final CodeService codeService;
    private final VoteService voteService;
    @Autowired
    public CodeController(CodeService codeService, UserService userService, VoteService voteService){
        this.userService = userService;
        this.codeService = codeService;
        this.voteService = voteService;
    }

    @GetMapping("/codes/create")
    public String post(@ModelAttribute("codeForm") CodeForm codeForm){
        return "codes/create";
    }

    @PostMapping("/codes/create")
    public String post(@ModelAttribute @Valid CodeForm codeForm, BindingResult result)  throws IOException {
        if (result.hasErrors()){
            return "codes/create";
        }
        codeService.create(userService.currentUser(),
                codeForm.getTitle(),
                codeForm.getDescription(),
                new String(codeForm.getJavaFile().getBytes(),StandardCharsets.UTF_8),
                new String(codeForm.getUnitFile().getBytes(), StandardCharsets.UTF_8));
        return "redirect:/";
    }


    @GetMapping("/codes/{codeId}")
    public String code(@PathVariable("codeId") @Valid long codeId, Model model){
        model.addAttribute("auth", userService.getInformation(userService.currentUser().getUsername()));
        var code = codeService.getInformation(codeId);
        model.addAttribute("code", code);
        return "codes/codeReview";
    }




    @PostMapping("/codes/vote/{codeId}")
    public String codeVoted(@PathVariable("codeId") @Valid long codeId,
                            @RequestParam("voteType") Vote.VoteType voteType){
        voteService.codeVoted(userService.currentUser().getId(), codeId, voteType);
        return "redirect:/";
    }


}
