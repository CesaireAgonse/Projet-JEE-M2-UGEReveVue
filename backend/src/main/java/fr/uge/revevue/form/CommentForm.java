package fr.uge.revevue.form;

public class CommentForm {

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
