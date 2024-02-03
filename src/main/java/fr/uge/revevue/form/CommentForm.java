package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;

public class CommentForm {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
