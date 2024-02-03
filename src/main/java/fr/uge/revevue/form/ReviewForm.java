package fr.uge.revevue.form;

import fr.uge.revevue.entity.Review;

public class ReviewForm {
    private String content;

    public ReviewForm(String content) {
        this.content = content;
    }

    public ReviewForm() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
