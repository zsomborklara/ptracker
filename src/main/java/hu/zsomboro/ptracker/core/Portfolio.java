package hu.zsomboro.ptracker.core;

import com.google.common.base.Predicates;
import com.google.common.collect.*;
import hu.zsomboro.ptracker.core.security.Instrument;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.*;

public class Portfolio {

  public static final Portfolio EMPTY = new Portfolio(ImmutableSet.of(), Cash.ZERO, "EMPTY", 0L);

  private final Set<Constituent> constituents;
  private final Cash cash;
  private final String name;
  private final long id;

  public Portfolio(Set<Constituent> constituents, Cash cash, String name, long id) {
    this.constituents = ImmutableSet.copyOf(constituents);
    this.cash = cash;
    this.name = name;
    this.id = id;
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

  public Portfolio withCash(Cash cash) {
    return new Portfolio(constituents, cash, name, id);
  }

  public Portfolio withInstrument(final Instrument instrument, int number) {

    final ImmutableSet.Builder<Constituent> builder = ImmutableSet.builder();

    Constituent current = getConstituent(instrument);
    if (current != null) {

      builder.addAll(filter(current).collect(Collectors.toList()));
      builder.add(current.add(number));
    } else {
      builder.addAll(constituents);
      builder.add(new Constituent(number, instrument));
    }

    return new Portfolio(builder.build(), cash, name, id);
  }

  public Portfolio remove(Instrument instrument, int number) {

    Constituent current = getConstituent(instrument);

    if (current != null) {
      final ImmutableSet.Builder<Constituent> builder = ImmutableSet.builder();
      builder.addAll(filter(current).collect(Collectors.toList()));
      Constituent substracted = current.substract(number);
      if (substracted.number != 0) {
        builder.add(substracted);
      }
      ImmutableSet<Constituent> newConstituents = builder.build();
      return newConstituents.isEmpty() ? Portfolio.EMPTY : new Portfolio(newConstituents, cash, name, id);
    }

    return this;
  }

  public Set<Constituent> getConstituents() {
    return constituents;
  }

  @Override
  public String toString() {
    return "Portfolio [name=" + name + "]";
  }

  private Stream<Constituent> filter(Constituent existing) {
    return constituents.stream().filter(Predicates.not(constituent -> Objects.equals(constituent, existing)));
  }

  public long getId() {
    return id;
  }

  public record Constituent(int number, Instrument instrument) {

    public Constituent {
      checkNotNull(instrument, "Instrument is missing");
      checkArgument(number >= 0, "Cannot set a negative number of instruments");
    }

    public Constituent add(int number) {
      checkArgument(number >= 0, "Cannot add a negative number of instruments");
      return new Constituent(this.number + number, this.instrument);
    }

    public Constituent substract(int number) {
      checkArgument(number > 0, "Cannot substract a negative number of instruments");
      checkState(this.number >= number);
      return new Constituent(this.number - number, this.instrument);
    }

  }

  public static class Builder {

    private final Set<Constituent> constituents = Sets.newHashSet();
    private Cash cash = Cash.ZERO;
    private String name;
    private long id;

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withId(long id) {
      this.id = id;
      return this;
    }

    public Builder add(Cash cash) {
      this.cash = cash;
      return this;
    }

    public Builder add(Instrument instrument, int number) {
      Optional<Constituent> existing = constituents.stream().filter(c -> c.instrument.equals(instrument)).findFirst();
      if(existing.isPresent()) {
        Constituent constituent = existing.get();
        constituents.remove(constituent);
        constituents.add(constituent.add(number));
      } else {
        constituents.add(new Constituent(number, instrument));
      }
      return this;
    }

    public Builder add(Constituent constituent) {
      constituents.add(constituent);
      return this;
    }

    public Portfolio build() {
      Portfolio portfolio = new Portfolio(constituents, cash, name, id);
      constituents.clear();
      return portfolio;
    }
  }
}
