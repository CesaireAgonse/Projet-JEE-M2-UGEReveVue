package fr.uge.revevue.controller;

import fr.uge.revevue.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class CodeController {
    private final CodeService codeService;
    @Autowired
    public CodeController(CodeService codeService){
        this.codeService = codeService;
    }

    @GetMapping("/codes/create")
    public String codeForm(){
        return "codes/create";
    }

    @PostMapping("/codes/create")
    public String codeForm(@RequestParam("javaFile") MultipartFile javaFile, @RequestParam("unitFile") MultipartFile unitFile) throws IOException {
        var javaFileContent = new String(javaFile.getBytes(), StandardCharsets.UTF_8);
        var unitFileContent = new String(unitFile.getBytes(), StandardCharsets.UTF_8);
        System.out.println(javaFileContent);
        System.out.println(unitFileContent);
        codeService.create(javaFileContent, unitFileContent);
        return "redirect:/";
    }


}
