package com.wolox.training.exceptions;

public class NotOwnedException extends RuntimeException {

  public NotOwnedException(String message) {
    super(message);
  }
}
