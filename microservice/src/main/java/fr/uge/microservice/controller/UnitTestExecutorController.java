package fr.uge.microservice.controller;

import fr.uge.microservice.form.UnitTestClassForm;
import fr.uge.microservice.information.UnitTestResultInformation;
import fr.uge.microservice.service.UnitTestExecutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("api/v1")
public class UnitTestExecutorController {
    private final UnitTestExecutorService unitTestExecutorService;

    UnitTestExecutorController(UnitTestExecutorService unitTestExecutorService){
        this.unitTestExecutorService = unitTestExecutorService;
    }

    @PostMapping("/execute")
    public ResponseEntity<UnitTestResultInformation> execute(@RequestBody @Valid UnitTestClassForm unitTestClassForm, BindingResult result) {
        if (result.hasErrors()){
            ResponseEntity.badRequest();
        }
        try {
            return ResponseEntity.ok(unitTestExecutorService.executeWithVerification(unitTestClassForm.getJavaCode(), unitTestClassForm.getUnitCode()));
        } catch (TimeoutException e) {
            return ResponseEntity.status(504).build();
        }
    }
}
