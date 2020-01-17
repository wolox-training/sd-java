package com.wolox.training.exceptions;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler({NotFoundException.class})
  protected ResponseEntity<Object> handleBookNotFound(NotFoundException e) {
    HttpStatus notFound = HttpStatus.NOT_FOUND;
    RestExceptionFormat restExceptionFormat = new RestExceptionFormat(
        e.getMessage(),
        notFound,
        ZonedDateTime.now()
    );

    return new ResponseEntity<>(restExceptionFormat, notFound);
  }

  @ExceptionHandler({IdMismatchException.class})
  protected ResponseEntity<Object> handleBookIdMismatch(IdMismatchException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    RestExceptionFormat restExceptionFormat = new RestExceptionFormat(
        e.getMessage(),
        badRequest,
        ZonedDateTime.now()
    );

    return new ResponseEntity<>(restExceptionFormat, badRequest);
  }

  @ExceptionHandler({AlreadyOwnedException.class})
  protected ResponseEntity<Object> handleBookAlreadyOwned(AlreadyOwnedException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    RestExceptionFormat restExceptionFormat = new RestExceptionFormat(
        e.getMessage(),
        badRequest,
        ZonedDateTime.now()
    );

    return new ResponseEntity<>(restExceptionFormat, badRequest);
  }

  @ExceptionHandler({NotOwnedException.class})
  protected ResponseEntity<Object> handleBookNotOwned(NotOwnedException e) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    RestExceptionFormat restExceptionFormat = new RestExceptionFormat(
        e.getMessage(),
        badRequest,
        ZonedDateTime.now()
    );

    return new ResponseEntity<>(restExceptionFormat, badRequest);
  }
}
