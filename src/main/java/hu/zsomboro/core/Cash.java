package hu.zsomboro.core;

public record Cash(double amount, String currency) {

  public static final Cash ZERO = new Cash(0, "HUF");

}
