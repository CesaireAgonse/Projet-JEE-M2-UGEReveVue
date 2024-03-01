package fr.uge.revevue.controller.rest;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.CodeInformation;
import fr.uge.revevue.information.FilterInformation;
import fr.uge.revevue.information.ReviewInformation;
import fr.uge.revevue.information.SimpleUserInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import fr.uge.revevue.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/codes")
public class CodeRestController {
    private final UserService userService;
    private final CodeService codeService;
    private final VoteService voteService;

    @Autowired
    public CodeRestController(CodeService codeService, UserService userService, VoteService voteService) {
        this.userService = userService;
        this.codeService = codeService;
        this.voteService = voteService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{codeId}")
    public ResponseEntity<CodeInformation> code(@PathVariable("codeId") @Valid long codeId) throws IOException {
        var code = codeService.getInformation(codeId);
        if (code == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(code);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{codeId}")
    public ResponseEntity<Void> codeDeleted(@PathVariable("codeId") @Valid long codeId) {
        codeService.delete(codeId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Void> post(@ModelAttribute @Valid CodeForm codeForm, BindingResult result) throws IOException {
        codeService.create(userService.currentUser().getId(),
                codeForm.getTitle(),
                codeForm.getDescription(),
                codeForm.getJavaFile(),
                codeForm.getUnitFile());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/filter")
    public ResponseEntity<FilterInformation> filter(@RequestParam(value = "sortBy", required = false)String sortBy,
                           @RequestParam(value = "q", required = false, defaultValue = "")String query,
                           @RequestParam(value = "pageNumber", required = false)Integer pageNumber) {
        var user = userService.currentUser();
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        List<CodeInformation> codes;

        switch (sortBy != null ? sortBy : "") {
            // Display all codes by newest
            case "newest" -> {
                codes = codeService.findWithKeywordByNewest(query, pageNumber, CodeService.LIMIT);
            }
            // Display all codes by relevance
            case "relevance"-> {
                codes = codeService.findWithKeywordByScore(query, pageNumber, CodeService.LIMIT);
            }
            default -> {
                if(user != null) {
                    // Display codes from follows
                    codes = codeService.getCodeFromFollowed(user, query, pageNumber, CodeService.LIMIT);
                }
                else {
                    // Display all codes
                    codes = codeService.findWithKeyword(query, pageNumber, CodeService.LIMIT);
                }
            }
        }
        return ResponseEntity.ok(new FilterInformation(codes, sortBy, query, pageNumber));
    }
}
