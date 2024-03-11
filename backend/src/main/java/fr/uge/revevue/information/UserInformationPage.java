package fr.uge.revevue.information;

import java.util.List;

public record UserInformationPage(List<UserInformation> users, int pageNumber, int maxPageNumber) {
}
