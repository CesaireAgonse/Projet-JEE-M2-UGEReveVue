package fr.uge.revevue.controller.rest;

import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.information.code.CodeInformation;
import fr.uge.revevue.information.code.FilterInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/codes")
public class CodeRestController {
    private final CodeService codeService;
    private final UserService userService;

    @Autowired
    public CodeRestController(CodeService codeService, UserService userService) {
        this.codeService = codeService;
        this.userService = userService;
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

    @PreAuthorize("permitAll()")
    @GetMapping("/filter")
    public ResponseEntity<FilterInformation> filter(@RequestParam(value = "sortBy", required = false) String sortBy,
                                                    @RequestParam(value = "q", required = false, defaultValue = "") String query,
                                                    @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        return ResponseEntity.ok(userService.filter(sortBy, query, pageNumber));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Void> post(@ModelAttribute @Valid CodeForm codeForm) throws IOException {
        codeService.create(userService.currentUser().getId(),
                codeForm.getTitle(),
                codeForm.getDescription(),
                codeForm.getJavaFile(),
                codeForm.getUnitFile());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{codeId}")
    public ResponseEntity<Void> codeDeleted(@PathVariable("codeId") @Valid long codeId) {
        if (!codeService.isExisted(codeId)){
            return ResponseEntity.notFound().build();
        }
        codeService.delete(codeId);
        return ResponseEntity.ok().build();
    }
}