package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentForm {

    @NotBlank
    @NotNull
    @NotEmpty
    private String content;

    private String codeSelection;

    public CommentForm(){}

    public CommentForm(String content, String codeSelection) {
        this.content = content;
        this.codeSelection = codeSelection;
    }

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
