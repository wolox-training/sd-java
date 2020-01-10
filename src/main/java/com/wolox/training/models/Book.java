package com.wolox.training.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
  @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
  private Long id;

  @Column
  private String genre;

  @Column(nullable=false)
  private  String author;

  @Column(nullable=false)
  private  String image;

  @Column(nullable=false)
  private  String title;

  @Column(nullable=false)
  private String subtitle;

  @Column(nullable=false)
  private String publisher;

  @Column(nullable=false)
  private String year;

  @Column(nullable=false)
  private  Integer pages;

  @Column(nullable=false, unique = true)
  private  String isbn;

  public Book() {
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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
    this.author = author;
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

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public Integer getPages() {
    return pages;
  }

  public void setPages(Integer pages) {
    this.pages = pages;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Long getId() {
    return id;
  }
}
