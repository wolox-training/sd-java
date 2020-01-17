package com.wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;

import com.wolox.training.Constant;
import com.wolox.training.exceptions.AlreadyOwnedException;
import com.wolox.training.exceptions.NotOwnedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
  @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
  private Long id;

  @Column(nullable = false, unique = true)
  @NotNull
  private String userName;

  @Column(nullable = false)
  @NotNull
  private String name;

  @Column(nullable = false)
  @NotNull
  private LocalDate birthDate;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  private List<Book> books;

  public User() {
    books = new ArrayList<>();
  }

  public User(String userName, String name, LocalDate birthDate,
      List<Book> books) {
    this.setUserName(userName);
    this.setName(name);
    this.setBirthDate(birthDate);
    this.setBooks(books);
  }

  public Long getId() {
    return id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    checkArgument(userName != null && !userName.isEmpty(), Constant.NOT_NULL_MESSAGE, "userName");
    this.userName = userName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    checkArgument(name != null && !name.isEmpty(), Constant.NOT_NULL_MESSAGE, "name");
    this.name = name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    checkArgument(birthDate != null && birthDate.isBefore(LocalDate.now()),
        Constant.INVALID, "date");
    this.birthDate = birthDate;
  }

  public List<Book> getBooks() {
    return books;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }

  public void addBook(Book book) {
    if (books.contains(book)) {
      throw new AlreadyOwnedException("Book Already Owned");
    } else {
      books.add(book);
    }
  }

  public void removeBook(Book book) {
    if (!books.contains(book)) {
      throw new NotOwnedException("Book Not Owned");
    } else {
      books.remove(book);
    }
  }
}
