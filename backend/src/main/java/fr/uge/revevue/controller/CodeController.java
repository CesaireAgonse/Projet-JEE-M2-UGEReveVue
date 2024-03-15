package fr.uge.revevue.controller;

import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.PagingInformation;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/codes/create")
    public String post(@ModelAttribute("codeForm") CodeForm codeForm, Model model){
        model.addAttribute("auth", SimpleUserInformation.from(userService.currentUser()));
        return "codes/create";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/codes/{codeId}")
    public String code(@PathVariable("codeId") @Valid long codeId,
                       @ModelAttribute("commentForm") CommentForm commentForm,
                       @ModelAttribute("reviewForm") ReviewForm reviewForm,
                       @RequestParam(value = "reviewPageNumber", required = false) Integer reviewPageNumber,
                       @RequestParam(value = "commentPageNumber", required = false) Integer commentPageNumber,
                       Model model){
        var user = userService.currentUser();
        if (user != null){
            model.addAttribute("auth", SimpleUserInformation.from(user));
        }
        var code = codeService.getInformation(codeId);
        if (code == null){
            throw new IllegalStateException("code not found");
        }
        model.addAttribute("code", code);

        PagingInformation pagingInfo = new PagingInformation(0, reviewPageNumber, commentPageNumber,0);
        pagingInfo = pagingInfo.setDefaultsIfNull();
        model.addAttribute("pagingInfo", pagingInfo);

        var reviewsFromPost = reviewService.getReviews(code.id(), pagingInfo.reviewPageNumber());
        var commentsFromPost = commentService.getComments(code.id(), pagingInfo.commentPageNumber());
        model.addAttribute("reviewsFromPost", reviewsFromPost);
        model.addAttribute("commentsFromPost", commentsFromPost);
        return "codes/codeReview";
    }

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/codes/vote/{codeId}")
    public String codeVoted(@PathVariable("codeId") @Valid long codeId,
                            @RequestParam("voteType") Vote.VoteType voteType,
                            BindingResult result){
        if (result.hasErrors()){
            return "redirect:/codes/" + codeId;
        }
        if (!codeService.isExisted(codeId)){
            return "redirect:/";
        }
        voteService.postVotedWithOptimisticLock(userService.currentUser().getId(), codeId, voteType);
        return "redirect:/codes/" + codeId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/codes/comment/{codeId}")
    public String codeCommented(@PathVariable("codeId") @Valid long codeId,
                                @ModelAttribute("commentForm") @Valid CommentForm commentForm,
                                BindingResult result){
        if (result.hasErrors()){
            return "redirect:/codes/" + codeId;
        }
        if (!codeService.isExisted(codeId)){
            return "redirect:/";
        }
        commentService.postCommented(userService.currentUser().getId(),codeId,commentForm.getContent(), commentForm.getCodeSelection());
        return "redirect:/codes/" + codeId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/codes/review/{codeId}")
    public String codeReviewed(@PathVariable("codeId") @Valid long codeId,
                               @ModelAttribute("reviewForm") ReviewForm reviewForm,
                               BindingResult result){
        if (result.hasErrors()){
            return "redirect:/codes/" + codeId;
        }
        if (!codeService.isExisted(codeId)){
            return "redirect:/";
        }
        reviewService.create(userService.currentUser().getId(),codeId, reviewForm.getTitle(), reviewForm.getContent());
        return "redirect:/codes/" + codeId;
    }
}
