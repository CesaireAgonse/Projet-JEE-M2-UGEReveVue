package fr.uge.revevue.form;

public class CommentForm {
    private String content;

    public CommentForm(String content) {
        this.content = content;
    }

    public CommentForm() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
