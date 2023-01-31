package hu.zsomboro.ptracker.core.security;

import java.time.LocalDate;

public interface HasMaturity {

  LocalDate getMaturity();

  double getInterestRate();

}
