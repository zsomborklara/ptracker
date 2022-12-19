package hu.zsomboro.core.security;

import java.time.LocalDate;

public interface HasMaturity {

  LocalDate getMaturityDate();

  double getInterestRate();

}
