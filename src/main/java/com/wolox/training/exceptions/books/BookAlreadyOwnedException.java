package com.wolox.training.exceptions.books;

public class BookAlreadyOwnedException extends RuntimeException {
  public BookAlreadyOwnedException(String message) {
    super(message);
  }
}
