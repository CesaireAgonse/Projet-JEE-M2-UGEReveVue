package fr.uge.revevue.controller;

import fr.uge.revevue.form.PasswordForm;
import fr.uge.revevue.information.PagingInformation;
import fr.uge.revevue.information.user.AuthInformation;
import fr.uge.revevue.information.user.SimpleUserInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.CommentService;
import fr.uge.revevue.service.ReviewService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/password")
    public String password(@ModelAttribute("passwordForm") PasswordForm passwordForm, Model model){
        model.addAttribute("auth", SimpleUserInformation.from(userService.currentUser()));
        return "users/password";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/users/{username}")
    public String information(@PathVariable String username,
                              @RequestParam(value = "codePageNumber", required = false) Integer codePageNumber,
                              @RequestParam(value = "reviewPageNumber", required = false) Integer reviewPageNumber,
                              @RequestParam(value = "commentPageNumber", required = false) Integer commentPageNumber,
                              @RequestParam(value = "followedPageNumber", required = false) Integer followedPageNumber,
                              Model model){
        var userInformation = userService.getInformation(username);
        if (userInformation == null){
            return "redirect:/";
        }
        var user = userService.currentUser();
        if (user != null){
            model.addAttribute("auth", AuthInformation.from(user));
        }
        model.addAttribute("user", userInformation);
        model.addAttribute("followedPageInformation", userService.users(userInformation.username(), followedPageNumber));
        model.addAttribute("codePageInformation", userService.codes(userInformation.username(), followedPageNumber));
        model.addAttribute("reviewPageInformation", userService.reviews(userInformation.username(), reviewPageNumber));
        model.addAttribute("commentPageInformation", userService.comments(userInformation.username(), commentPageNumber));
        return "users/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public String password(@ModelAttribute("passwordForm") @Valid PasswordForm passwordForm, BindingResult result, Model model){
        var user = userService.currentUser();
        model.addAttribute("auth", AuthInformation.from(user));
        if (result.hasErrors()){
            return "users/password";
        }
        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())){
            result.rejectValue("confirmPassword", "error.passwordForm", "The confirmation of the new password does not match the new password entered.");
            return "users/password";
        }
        if(userService.matchesPassword(passwordForm.getNewPassword(), user.getPassword())){
            result.rejectValue("newPassword", "error.passwordForm", "The new password should be different from the current password.");
            return "users/password";
        }
        if (!userService.matchesPassword(passwordForm.getCurrentPassword(), user.getPassword())) {
            result.rejectValue("currentPassword", "error.passwordForm", "The current password you entered is incorrect. Please try again.");
            return "users/password";
        }
        var userInformation = userService.modifyPassword(passwordForm.getCurrentPassword(), passwordForm.getNewPassword());
        return "redirect:/users/" + userInformation.username();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("users/follow/{username}")
    public String follow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return "redirect:/";
        }
        userService.follow(userService.currentUser().getUsername(), username);
        return "redirect:/users/" + username;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("users/unfollow/{username}")
    public String unfollow(@PathVariable String username){
        if (!userService.isExisted(username)){
            return "redirect:/";
        }
        userService.unfollow(userService.currentUser().getUsername(), username);
        return "redirect:/users/" + username;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{username}")
    public String delete(@PathVariable("username") @Valid String username) {
        if (!userService.isExisted(username)){
            return "redirect:/";
        }
        userService.delete(username);
        return "redirect:/";
    }
}
