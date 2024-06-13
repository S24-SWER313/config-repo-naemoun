package com.naim.userservice.users;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(Long id) {
    super("Could not find user " + id);
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}