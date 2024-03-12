package fr.uge.revevue.information.review;

import java.util.List;

public record ReviewPageInformation(List<ReviewInformation> reviews, int pageNumber, int maxPageNumber) {}
