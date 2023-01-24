package hu.zsomboro.ptracker.core;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import hu.zsomboro.core.security.Instrument;

public class Portfolio {

  public static final Portfolio EMPTY = new Portfolio();

  private final Set<Constituent> constituents;
  private final Cash cash;
  private final String name;

  public Portfolio(Set<Constituent> constituents, Cash cash, String name) {
    this.constituents = ImmutableSet.copyOf(constituents);
    this.cash = cash;
    this.name = name;
  }

  public Portfolio(Map<Instrument, Integer> instruments, Cash cash, String name) {
    final ImmutableSet.Builder<Constituent> builder = ImmutableSet.<Constituent>builder();
    instruments.entrySet().stream().map(e -> new Constituent(e.getValue(), e.getKey())).forEach(c -> builder.add(c));
    constituents = builder.build();
    this.cash = cash;
    this.name = name;
  }

  private Portfolio() {
    constituents = ImmutableSet.of();
    cash = Cash.ZERO;
    name = "EMPTY";
  }

  public Portfolio empty() {
    return EMPTY;
  }

  public Constituent getConstituent(Instrument instrument) {
    return Iterables.find(constituents, c -> c.instrument.equals(instrument), null);
  }

  public Cash getCash() {
    return cash;
  }

  public String getName() {
    return this.name;
  }

  public boolean hasInstrument(Instrument instrument) {
    return constituents.stream().anyMatch(c -> c.instrument.equals(instrument));
  }

  public Portfolio add(Cash cash) {
    return new Portfolio(constituents, cash, name);
  }

  public Portfolio add(final Instrument instrument, int number) {

    final ImmutableSet.Builder<Constituent> builder = ImmutableSet.<Constituent>builder();

    Constituent current = getConstituent(instrument);
    if (current != null) {

      builder.addAll(filter(current));
      builder.add(current.add(number));
    } else {
      builder.addAll(constituents);
      builder.add(new Constituent(number, instrument));
    }

    return new Portfolio(builder.build(), cash, name);
  }

  public Portfolio remove(Instrument instrument, int number) {

    Constituent current = getConstituent(instrument);

    if (current != null) {
      final ImmutableSet.Builder<Constituent> builder = ImmutableSet.<Constituent>builder();
      builder.addAll(filter(current));
      Constituent substracted = current.substract(number);
      if (substracted.number != 0) {
        builder.add(substracted);
      }
      ImmutableSet<Constituent> newConstituents = builder.build();
      return newConstituents.isEmpty() ? Portfolio.EMPTY : new Portfolio(newConstituents, cash, name);
    }

    return this;
  }

  public Set<Constituent> getConstituents() {
    return constituents;
  }

  private FluentIterable<Constituent> filter(Constituent existing) {
    return FluentIterable.from(constituents).filter(Predicates.not(Predicates.equalTo(existing)));
  }

  public class Constituent {

    private final int number;
    private final Instrument instrument;

    public Constituent(int number, Instrument instrument) {
      Preconditions.checkNotNull(instrument, "Instrument is missing");
      Preconditions.checkArgument(number >= 0, "Cannot set a negative number of instruments");
      this.number = number;
      this.instrument = instrument;
    }

    public Constituent add(int number) {
      Preconditions.checkArgument(number >= 0, "Cannot add a negative number of instruments");
      return new Constituent(this.number + number, this.instrument);
    }

    public Constituent substract(int number) {
      Preconditions.checkArgument(number > 0, "Cannot substract a negative number of instruments");
      Preconditions.checkState(this.number >= number);
      return new Constituent(this.number - number, this.instrument);
    }

    public int getNumber() {
      return number;
    }

    public Instrument getInstrument() {
      return instrument;
    }

  }

  public static class Builder {

    private Map<Instrument, Integer> instruments = Maps.newHashMap();
    private Cash cash = Cash.ZERO;
    private String name;

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder add(Cash cash) {
      this.cash = cash;
      return this;
    }

    public Builder add(Instrument instrument, int number) {
      Integer current = instruments.get(instrument);
      if (current != null) {
        instruments.put(instrument, current + number);
      } else {
        instruments.put(instrument, number);
      }
      return this;
    }

    public Portfolio build() {

      Portfolio portfolio = new Portfolio(instruments, cash, name);
      instruments.clear();
      return portfolio;
    }
  }

}
