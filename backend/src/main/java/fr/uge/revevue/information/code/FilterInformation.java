package fr.uge.revevue.information.code;

import java.util.List;

public record FilterInformation(List<CodeInformation> codes, String sortBy, String q, int pageNumber, int maxPageNumber, int resultNumber) {}
