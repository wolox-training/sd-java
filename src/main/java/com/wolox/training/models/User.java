package com.wolox.training.models;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
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

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
  @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
  private Long id;

  @Column(nullable = false, unique = true)
  private String userName;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birthDate;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  private List<Book> books;

  public User() {
    books = new ArrayList<>();
  }

  public User(String userName, String name, LocalDate birthDate,
      List<Book> books) {
    this.userName = userName;
    this.name = name;
    this.birthDate = birthDate;
    this.books = books;
  }

  public Long getId() {
    return id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    Preconditions
        .checkArgument(userName != null && !userName.isEmpty(), Constant.NOT_NULL_MESSAGE,
            "userName");
    this.userName = userName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    Preconditions
        .checkArgument(name != null && !name.isEmpty(), Constant.NOT_NULL_MESSAGE, "name");
    this.name = name;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = checkNotNull(birthDate, Constant.NOT_NULL_MESSAGE, "birthDate");
  }

  public List<Book> getBooks() {
    return books;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }

  public void addBook(Book book) throws AlreadyOwnedException {
    if (books.contains(book)) {
      throw new AlreadyOwnedException("Book Already Owned");
    } else {
      books.add(book);
    }
  }

  public void removeBook(Book book) throws NotOwnedException {
    if (!books.contains(book)) {
      throw new NotOwnedException("Book Not Owned");
    } else {
      books.remove(book);
    }
  }
}
