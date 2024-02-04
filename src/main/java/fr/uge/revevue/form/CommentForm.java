package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;

public class CommentForm {

    @NotBlank
    private String content;

    private String codeSelection;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCodeSelection() {
        return codeSelection;
    }

    public void setCodeSelection(String codeSelection) {
        this.codeSelection = codeSelection;
    }
}
