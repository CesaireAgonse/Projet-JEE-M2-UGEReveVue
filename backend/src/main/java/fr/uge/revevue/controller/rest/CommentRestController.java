package fr.uge.revevue.controller.rest;

import fr.uge.revevue.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/comments")
public class CommentRestController {
    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") long commentId) {
        if (!commentService.isExisted(commentId)){
            return ResponseEntity.notFound().build();
        }
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}
