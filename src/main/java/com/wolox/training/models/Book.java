package com.wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.wolox.training.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

@Entity
@ApiModel(description = "Books from the OpenLibrary & WBooks data base")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
  @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
  private Long id;

  @Column
  @ApiModelProperty(notes = "i.e horror, comedy, drama, etc...")
  private String genre;

  @Column(nullable = false)
  @ApiModelProperty(notes = "Name of the book's writer")
  private String author;

  @Column(nullable = false)
  @ApiModelProperty(notes = "Book's cover picture")
  private String image;

  @Column(nullable = false)
  @ApiModelProperty(notes = "Book's title")
  private String title;

  @Column(nullable = false)
  @ApiModelProperty(notes = "Book's subtitle")
  private String subtitle;

  @Column(nullable = false)
  @ApiModelProperty(notes = "Book's publishing house name")
  private String publisher;

  @Column(nullable = false)
  @ApiModelProperty(notes = "Book's release year")
  private String year;

  @Column(nullable = false)
  @ApiModelProperty(notes = "Book's number of pages")
  private Integer pages;

  @Column(nullable = false, unique = true)
  @ApiModelProperty(notes = "Book's unique isbn identifier")
  private String isbn;

  @ManyToMany(mappedBy = "books")
  @JsonIgnore
  private List<User> users;

  public Book() {
    users = new ArrayList<>();
  }

  public Book(String genre, String author, String image, String title, String subtitle,
      String publisher, String year, Integer pages, String isbn,
      List<User> users) {
    this.genre = genre;
    this.author = author;
    this.image = image;
    this.title = title;
    this.subtitle = subtitle;
    this.publisher = publisher;
    this.year = year;
    this.pages = pages;
    this.isbn = isbn;
    this.users = users;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = checkNotNull(image);
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    Preconditions
        .checkArgument(author != null && !author.isEmpty(), Constant.NOT_NULL_MESSAGE, "author");
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = checkNotNull(title);
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = checkNotNull(subtitle);
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = checkNotNull(publisher);
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = checkNotNull(year);
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = checkNotNull(pages);
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = checkNotNull(isbn);
  }

  public Long getId() {
    return id;
  }
}
