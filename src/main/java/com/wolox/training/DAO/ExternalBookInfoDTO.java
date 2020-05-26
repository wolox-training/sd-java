package com.wolox.training.DAO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalBookInfoDTO {

  private String ISBN;
  private String title;
  private String subtitle;
  @JsonProperty("number_of_pages")
  private Integer numberOfPages;
  @JsonProperty("publish_date")
  private String publishDate;
  private PublisherDTO[] publishers;
  private AuthorsDTO[] authors;

  public ExternalBookInfoDTO(String ISBN, String title, String subtitle,
      PublisherDTO[] publishers, AuthorsDTO[] authors, Integer numberOfPages,
      String publishDate) {
    this.ISBN = ISBN;
    this.title = title;
    this.subtitle = subtitle;
    this.publishers = publishers;
    this.authors = authors;
    this.numberOfPages = numberOfPages;
    this.publishDate = publishDate;
  }

  public ExternalBookInfoDTO() {
  }

  public String getISBN() {
    return ISBN;
  }

  public void setISBN(String ISBN) {
    this.ISBN = ISBN;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public PublisherDTO[] getPublishers() {
    return publishers;
  }

  public void setPublishers(PublisherDTO[] publishers) {
    this.publishers = publishers;
  }

  public AuthorsDTO[] getAuthors() {
    return authors;
  }

  public void setAuthors(AuthorsDTO[] authors) {
    this.authors = authors;
  }

  public Integer getNumberOfPages() {
    return numberOfPages;
  }

  public void setNumberOfPages(Integer numberOfPages) {
    this.numberOfPages = numberOfPages;
  }

  public String getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(String publishDate) {
    this.publishDate = publishDate;
  }
}
