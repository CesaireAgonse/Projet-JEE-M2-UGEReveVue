package fr.uge.revevue.information.user;

import java.util.List;

public record UserPageInformation(List<UserInformation> users, int pageNumber, int maxPageNumber) {
}
