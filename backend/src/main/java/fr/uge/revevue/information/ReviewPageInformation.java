package fr.uge.revevue.information;

import java.util.List;

public record ReviewPageInformation(List<ReviewInformation> reviews, int pageNumber) {}
