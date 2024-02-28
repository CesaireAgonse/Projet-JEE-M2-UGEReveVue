package fr.uge.revevue.information;

import java.util.List;

public record CommentPageInformation(List<CommentInformation> comments, int pageNumber) {}
