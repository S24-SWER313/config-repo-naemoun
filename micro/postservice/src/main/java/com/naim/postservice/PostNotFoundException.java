package com.naim.postservice;

class PostNotFoundException extends RuntimeException {

  public PostNotFoundException(Long id) {
    super("Could not find post " + id);
  }

  public PostNotFoundException(String message) {
    super(message);
}
}