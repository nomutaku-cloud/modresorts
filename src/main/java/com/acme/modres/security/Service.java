package com.acme.modres.security;

public class Service {
  public static final String OPERATION = "my-operation";

  public void operation() {
    // SecurityManager has been deprecated for removal since Java 17
    // and is no longer used in modern Java applications
    System.out.println("Operation is executed");
  }
}
