package fr.uge.revevue.information.review;

import java.util.List;

public record ReviewPageInformation(List<ReviewInformation> reviews, String sortBy, int pageNumber, int maxPageNumber, int resultNumber) {}
