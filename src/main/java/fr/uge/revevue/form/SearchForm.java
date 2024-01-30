package fr.uge.revevue.form;

import javax.validation.constraints.NotBlank;

public class SearchForm {
  @NotBlank
  private String searchQuery;
  
  public String getSearchQuery() {
    return searchQuery;
  }
  
  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }
}
