package com.wolox.training.exceptions;

public class AlreadyOwnedException extends RuntimeException {
  public AlreadyOwnedException(String message) {
    super(message);
  }
}
