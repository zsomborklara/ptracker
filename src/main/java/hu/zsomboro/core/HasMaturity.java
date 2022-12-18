package hu.zsomboro.core;

import java.time.LocalDate;

public interface HasMaturity {

  LocalDate getMaturityDate();

  double getInterestRate();

}
