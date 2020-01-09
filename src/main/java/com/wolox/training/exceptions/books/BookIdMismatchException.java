package com.wolox.training.exceptions.books;

public class BookIdMismatchException extends RuntimeException{
  public BookIdMismatchException(String message) {
    super(message);
  }
}
