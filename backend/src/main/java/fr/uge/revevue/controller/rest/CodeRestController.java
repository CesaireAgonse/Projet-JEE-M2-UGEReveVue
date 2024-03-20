package fr.uge.revevue.controller.rest;

import fr.uge.revevue.form.CodeForm;
import fr.uge.revevue.information.code.CodeInformation;
import fr.uge.revevue.information.code.FilterInformation;
import fr.uge.revevue.service.CodeService;
import fr.uge.revevue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<CodeInformation> get(@PathVariable("codeId") long codeId) throws IOException {
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
        return ResponseEntity.ok(codeService.filter(sortBy, query, pageNumber, userService.currentUser()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Void> create(@ModelAttribute @Valid CodeForm codeForm, BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        try {
            codeService.create(
                    codeForm.getTitle(),
                    codeForm.getDescription(),
                    codeForm.getJavaFile(),
                    codeForm.getUnitFile()
            );
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{codeId}")
    public ResponseEntity<Void> delete(@PathVariable("codeId") long codeId) {
        if (!codeService.isExisted(codeId)){
            return ResponseEntity.notFound().build();
        }
        codeService.delete(codeId);
        return ResponseEntity.ok().build();
    }
}