package com.wolox.training.support.factories;

import com.github.javafaker.Faker;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserFactory {

  private final Faker faker = new Faker();

  public UserFactory() {

  }

  public User createUserWithoutBooks() {
    List<Book> books = new ArrayList<>();
    Date fakeBirthday = faker.date().birthday(1, 120);
    LocalDate birthday = fakeBirthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String password = faker.dragonBall().character();
    return new User(
        faker.name().username(),
        faker.name().name(),
        birthday,
        books,
        password
    );
  }
}
