package fr.uge.revevue.form;

import fr.uge.revevue.entity.Review;

import javax.validation.constraints.NotBlank;

public class ReviewForm {

    @NotBlank
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
