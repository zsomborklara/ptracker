package hu.zsomboro.core.security;

import java.time.LocalDate;

public interface HasMaturity {

  LocalDate getMaturity();

  double getInterestRate();

}
