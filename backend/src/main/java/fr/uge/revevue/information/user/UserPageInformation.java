package fr.uge.revevue.information.user;

import java.util.List;

public record UserPageInformation(List<SimpleUserInformation> users, int pageNumber, int maxPageNumber, int resultNumber) {}
