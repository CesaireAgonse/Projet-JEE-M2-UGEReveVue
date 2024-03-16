package fr.uge.revevue.information.review;

import java.util.List;

public record ReviewContentPageInformation(List<ReviewContentInformation> reviewContent, int pageNumber, int maxPageNumber) {}
