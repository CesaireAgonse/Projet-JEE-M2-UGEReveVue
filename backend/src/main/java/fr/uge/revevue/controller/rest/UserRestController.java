package fr.uge.revevue.controller.rest;

import fr.uge.revevue.information.UpdatePasswordInformation;
import fr.uge.revevue.information.code.CodePageInformation;
import fr.uge.revevue.information.comment.CommentPageInformation;
import fr.uge.revevue.information.review.ReviewContentPageInformation;
import fr.uge.revevue.information.review.ReviewPageInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.information.user.UserPageInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.ReviewService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/users")
public class UserRestController {
    private final UserService userService;
    private final CodeService codeService;
    private final ReviewService reviewService;

    @Autowired
    public UserRestController(UserService userService, CodeService codeService, ReviewService reviewService){
        this.userService = userService;
        this.codeService = codeService;
        this.reviewService = reviewService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{username}")
    public ResponseEntity<UserInformation> get(@PathVariable String username){
        var userInformation = userService.getInformation(username);
        if (userInformation == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInformation);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/codes/{username}")
    public ResponseEntity<CodePageInformation> getCodes(@PathVariable("username") String username,
                                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(codeService.codes(username, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/reviews/{username}")
    public ResponseEntity<ReviewPageInformation> getReviews(@PathVariable("username") String username,
                                                            @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewService.reviews(username, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/comments/{username}")
    public ResponseEntity<CommentPageInformation> getComments(@PathVariable("username") String username,
                                                              @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.comments(username, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/reviews/contents/{username}")
    public ResponseEntity<ReviewContentPageInformation> getReviewContent(@PathVariable("username") String username,
                                                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewService.reviewsContents(username, pageNumber));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/followed/{username}")
    public ResponseEntity<UserPageInformation> getFollowed(@PathVariable("username") String username,
                                                           @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.users(username, pageNumber));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<UserPageInformation> getUser(@RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        return ResponseEntity.ok(userService.usersNonAdmin(pageNumber));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid UpdatePasswordInformation updatePasswordInformation, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        userService.modifyPassword(updatePasswordInformation.currentPassword(), updatePasswordInformation.newPassword());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/follow/{username}")
    public ResponseEntity<Void> follow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        userService.follow(userService.currentUser().getUsername(), username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unfollow/{username}")
    public ResponseEntity<Void> unfollow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        userService.unfollow(userService.currentUser().getUsername(), username);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/photo")
    public ResponseEntity<Void> changePhoto(@RequestParam("photo") MultipartFile photo){
        try {
            userService.changePhoto(photo.getBytes());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable("username") String username) {
        if (!userService.isExisted(username)){
            return ResponseEntity.notFound().build();
        }
        userService.delete(username);
        return ResponseEntity.ok().build();
    }
}
