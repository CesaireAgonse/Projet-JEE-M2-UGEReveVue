package fr.uge.revevue.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CodeForm {

    @NotBlank(message = "Please enter a title for your content.")
    private String title;

    @NotBlank(message = "Please provide a description for your content.")
    private String description;

    private MultipartFile javaFile;

    private MultipartFile unitFile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getJavaFile() {
        return javaFile;
    }

    public void setJavaFile(MultipartFile javaFile) {
        this.javaFile = javaFile;
    }

    public MultipartFile getUnitFile() {
        return unitFile;
    }

    public void setUnitFile(MultipartFile unitFile) {
        this.unitFile = unitFile;
    }
}
