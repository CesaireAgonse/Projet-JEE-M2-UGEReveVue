package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.UpdatePasswordInformation;
import fr.uge.revevue.information.code.CodePageInformation;
import fr.uge.revevue.information.code.FilterInformation;
import fr.uge.revevue.information.comment.CommentPageInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.information.user.UserPageInformation;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
public class UserRestController {
    private final UserService userService;
    @Autowired
    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public ResponseEntity<Void> password(@RequestBody @Valid UpdatePasswordInformation updatePasswordInformation, BindingResult result){
        if (result.hasErrors()){
            return  ResponseEntity.badRequest().build();
        }
        userService.modifyPassword(updatePasswordInformation.currentPassword(), updatePasswordInformation.newPassword());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{username}")
    public ResponseEntity<UserInformation> information(@PathVariable String username){
        var userInformation = userService.getInformation(username);
        if (userInformation == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userInformation);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/follow/{username}")
    public ResponseEntity<Void> follow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return ResponseEntity.badRequest().build();
        }
        userService.follow(userService.currentUser().getUsername(), username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unfollow/{username}")
    public ResponseEntity<Void> unfollow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return ResponseEntity.badRequest().build();
        }
        userService.unfollow(userService.currentUser().getUsername(), username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") @Valid long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/codes/{username}")
    public ResponseEntity<CodePageInformation> codes(@PathVariable("username") @Valid String username,
                                                     @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        return ResponseEntity.ok(userService.codes(username, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/reviews/{username}")
    public ResponseEntity<ReviewPageInformation> reviews(@PathVariable("username") @Valid String username,
                                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        return ResponseEntity.ok(userService.reviews(username, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/comments/{username}")
    public ResponseEntity<CommentPageInformation> comments(@PathVariable("username") @Valid String username,
                                                           @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        return ResponseEntity.ok(userService.comments(username, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/followed/{username}")
    public ResponseEntity<UserPageInformation> users(@PathVariable("username") @Valid String username,
                                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        return ResponseEntity.ok(userService.users(username, pageNumber));
    }
}
