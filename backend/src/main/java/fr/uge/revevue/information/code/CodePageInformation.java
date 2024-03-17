package fr.uge.revevue.information.code;

import java.util.List;

public record CodePageInformation(List<CodeInformation> codes, int pageNumber, int maxPageNumber, int resultNumber) {}
