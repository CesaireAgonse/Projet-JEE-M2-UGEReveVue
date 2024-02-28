package fr.uge.revevue.information;

import java.util.List;

public record FilterInformation(List<CodeInformation> codes, String sortBy, String q, int pageNumber) {}
