package fr.uge.revevue.controller.rest;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.form.CommentForm;
import fr.uge.revevue.form.ReviewForm;
import fr.uge.revevue.information.CodeInformation;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/codes")
public class CodeRestController {
    private final UserService userService;
    private final CodeService codeService;
    private final VoteService voteService;

    @Autowired
    public CodeRestController(CodeService codeService, UserService userService, VoteService voteService){
        this.userService = userService;
        this.codeService = codeService;
        this.voteService = voteService;
    }

    @GetMapping("/{codeId}")
    public ResponseEntity<CodeInformation> code(@PathVariable("codeId") @Valid long codeId)  throws IOException {
        var code = codeService.getInformation(codeId);
        if (code == null){
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

}
