package com.wolox.training;

public final class Constant {

  private Constant() {
  }

  // Books
  public static final String BOOK_NOT_FOUND   = "Book Not Found";
  public static final String BOOK_ID_MISMATCH = "Book Id Mismatch";

  // Users
  public static final String USER_NOT_FOUND   = "User Not Found";
  public static final String USER_ID_MISMATCH = "User Id Mismatch";

  // Swagger messages
  public static final String OK_MESSAGE             = "Operation successful";
  public static final String NOT_AUTHORIZED_MESSAGE = "You are not authorized to view the resource";
  public static final String NOT_FOUND_MESSAGE      = "The resource you were trying to reach is not found";
  public static final String FORBIDDEN_MESSAGE      = "Accessing the resource you were trying to reach is forbidden";
  public static final String CREATED_MESSAGE        = "Record successfully saved";
}
