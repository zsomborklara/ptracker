package hu.zsomboro.core;

import java.time.LocalDate;
import java.util.Currency;

public record Price(double value, LocalDate asOfDate, Currency priceCurrency) {
}
