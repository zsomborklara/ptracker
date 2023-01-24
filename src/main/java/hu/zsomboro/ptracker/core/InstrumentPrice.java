package hu.zsomboro.ptracker.core;

import java.time.LocalDate;

import hu.zsomboro.core.security.Instrument;

public record InstrumentPrice(LocalDate asOfDate, Instrument instrument, double price) {
}
