package fr.uge.revevue.information.comment;

import java.util.List;

public record CommentPageInformation(List<CommentInformation> comments, int pageNumber) {}
