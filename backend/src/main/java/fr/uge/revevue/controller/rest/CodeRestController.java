package fr.uge.revevue.controller.rest;

import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.information.code.CodeInformation;
import fr.uge.revevue.information.code.FilterInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import fr.uge.revevue.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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
    public ResponseEntity<Void> post(@ModelAttribute @Valid CodeForm codeForm) throws IOException {
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
                           @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        return ResponseEntity.ok(codeService.filter(sortBy, query, pageNumber));
    }
}
