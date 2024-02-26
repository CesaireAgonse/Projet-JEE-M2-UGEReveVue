package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.form.UnitTestClassForm;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@Controller
public class CodeController {
    private final UserService userService;
    private final CodeService codeService;
    private final VoteService voteService;
    private final CommentService commentService;

    private final ReviewService reviewService;
    @Autowired
    public CodeController(CodeService codeService, UserService userService, VoteService voteService, CommentService commentService, ReviewService reviewService){
        this.userService = userService;
        this.codeService = codeService;
        this.voteService = voteService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @GetMapping("/codes/create")
    public String post(@ModelAttribute("codeForm") CodeForm codeForm, Model model){
        model.addAttribute("auth", SimpleUserInformation.from(userService.currentUser()));
        return "codes/create";
    }

    @PostMapping("/codes/create")
    public String post(@ModelAttribute @Valid CodeForm codeForm, BindingResult result, Model model)  throws IOException {
        if (result.hasErrors()){
            return "codes/create";
        }
        if (codeForm.getJavaFile().isEmpty()){
            result.rejectValue("javaFile", "error.codeForm", "Please upload a file.");
            return "codes/create";
        }
        if (!Objects.requireNonNull(codeForm.getJavaFile().getResource().getFilename()).endsWith(".java")){
            result.rejectValue("javaFile", "error.codeForm", "Please upload a java file.");
            return "codes/create";
        }
        if (!codeForm.getUnitFile().isEmpty() &&
                !Objects.requireNonNull(codeForm.getUnitFile().getResource().getFilename()).endsWith(".java")){
            result.rejectValue("unitFile", "error.codeForm", "Please upload a java file.");
            return "codes/create";
        }
        codeService.create(
                userService.currentUser().getId(),
                codeForm.getTitle(),
                codeForm.getDescription(),
                codeForm.getJavaFile(),
                codeForm.getUnitFile());
        return "redirect:/";
    }

    @GetMapping("/codes/{codeId}")
    public String code(@PathVariable("codeId") @Valid long codeId,
                       @ModelAttribute("commentForm") CommentForm commentForm,
                       @ModelAttribute("reviewForm") ReviewForm reviewForm,
                       Model model){
        model.addAttribute("auth", SimpleUserInformation.from(userService.currentUser()));
        var code = codeService.getInformation(codeId);
        if (code == null){
            throw new IllegalStateException("code not found");
        }
        model.addAttribute("code", code);
        return "codes/codeReview";
    }

    @PostMapping("/codes/vote/{codeId}")
    public String codeVoted(@PathVariable("codeId") @Valid long codeId,
                            @RequestParam("voteType") Vote.VoteType voteType,
                            BindingResult result){
        if (result.hasErrors()){
            return "redirect:/codes/" + codeId;
        }
        voteService.postVoted(userService.currentUser().getId(), codeId, voteType);
        return "redirect:/";
    }

    @PostMapping("/codes/comment/{codeId}")
    public String codeCommented(@PathVariable("codeId") @Valid long codeId,
                                @ModelAttribute("commentForm") @Valid CommentForm commentForm,
                                BindingResult result){
        if (result.hasErrors()){
            return "redirect:/codes/" + codeId;
        }
        commentService.postCommented(userService.currentUser().getId(),codeId,commentForm.getContent(), commentForm.getCodeSelection());
        return "redirect:/codes/" + codeId;
    }

    @PostMapping("/codes/review/{codeId}")
    public String codeReviewed(@PathVariable("codeId") @Valid long codeId,
                               @ModelAttribute("reviewForm") @Valid ReviewForm reviewForm,
                               BindingResult result){
        if (result.hasErrors()){
            return "redirect:/codes/" + codeId;
        }
        reviewService.create(userService.currentUser().getId(),codeId,reviewForm.getContent());
        return "redirect:/codes/" + codeId;
    }
}
