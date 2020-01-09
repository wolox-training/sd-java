package com.wolox.training.exceptions.books;

public class BookNotFoundException extends RuntimeException{

  public BookNotFoundException(String message) {
    super(message);
  }
}
