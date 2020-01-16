package com.wolox.training.support.factories;

import com.github.javafaker.Faker;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import java.util.ArrayList;
import java.util.List;

public class BookFactory {

  private final Faker faker = new Faker();

  public BookFactory() {

  }

  public Book createBookWithOutUser() {
    List<User> users = new ArrayList<>();
    return new Book(
        faker.book().genre(),
        faker.book().author(),
        faker.dragonBall().character(),
        faker.book().title(),
        faker.book().title(),
        faker.book().publisher(),
        faker.date().toString(),
        faker.number().numberBetween(10, 1000),
        faker.code().isbn10(),
        users
    );
  }

}
