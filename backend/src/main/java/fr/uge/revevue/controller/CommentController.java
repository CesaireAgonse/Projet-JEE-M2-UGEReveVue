package fr.uge.revevue.controller;

import fr.uge.revevue.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("comments/{commentId}")
    public String delete(@PathVariable("commentId") long commentId) {
        if (!commentService.isExisted(commentId)){
            return "redirect:/";
        }
        commentService.delete(commentId);
        return "redirect:/";
    }
}
