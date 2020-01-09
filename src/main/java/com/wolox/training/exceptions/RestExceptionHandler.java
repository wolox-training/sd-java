package com.wolox.training.exceptions;

import com.wolox.training.exceptions.books.BookAlreadyOwnedException;
import com.wolox.training.exceptions.books.BookIdMismatchException;
import com.wolox.training.exceptions.books.BookNotFoundException;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler({ BookNotFoundException.class })
  protected ResponseEntity<Object> handleBookNotFound(BookNotFoundException e) {
    HttpStatus notFound = HttpStatus.NOT_FOUND;
    RestExceptionFormat restExceptionFormat = new RestExceptionFormat(
        e.getMessage(),
        notFound,
        ZonedDateTime.now()
    );

    return new ResponseEntity<>(restExceptionFormat, notFound);
  }

  @ExceptionHandler({ BookIdMismatchException.class })
  protected ResponseEntity<Object> handleBookIdMismatch(BookIdMismatchException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    RestExceptionFormat restExceptionFormat = new RestExceptionFormat(
        e.getMessage(),
        badRequest,
        ZonedDateTime.now()
    );

    return new ResponseEntity<>(restExceptionFormat, badRequest);
  }

  @ExceptionHandler({ BookAlreadyOwnedException.class })
  protected ResponseEntity<Object> handleBookAlreadyOwned(BookAlreadyOwnedException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    RestExceptionFormat restExceptionFormat = new RestExceptionFormat(
        e.getMessage(),
        badRequest,
        ZonedDateTime.now()
    );

    return new ResponseEntity<>(restExceptionFormat, badRequest);
  }
}
