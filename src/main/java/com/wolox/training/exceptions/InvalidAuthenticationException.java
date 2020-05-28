package com.wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid credentials")
public class InvalidAuthenticationException extends AuthenticationException {

  public InvalidAuthenticationException() {
    super("Invalid credentials");
  }
}
