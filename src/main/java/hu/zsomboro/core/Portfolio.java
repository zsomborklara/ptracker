package hu.zsomboro.core;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.persistence.entity.ConstituentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;

public class Portfolio {

  public static final Portfolio EMPTY = new Portfolio();

  private final Set<Constituent> constituents;
  private final Cash cash;

  public Portfolio(Set<Constituent> constituents, Cash cash) {
    this.constituents = ImmutableSet.copyOf(constituents);
    this.cash = cash;
  }

  public Portfolio(Map<Instrument, Integer> instruments, Cash cash) {
    final ImmutableSet.Builder<Constituent> builder = ImmutableSet.<Constituent>builder();
    instruments.entrySet().stream().map(e -> new Constituent(e.getValue(), e.getKey())).forEach(c -> builder.add(c));
    constituents = builder.build();
    this.cash = cash;
  }

  private Portfolio() {
    constituents = ImmutableSet.of();
    cash = Cash.ZERO;
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

  public boolean hasInstrument(Instrument instrument) {
    return constituents.stream().anyMatch(c -> c.instrument.equals(instrument));
  }

  public Portfolio add(Cash cash) {
    return new Portfolio(constituents, cash);
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

    return new Portfolio(builder.build(), cash);
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
      return newConstituents.isEmpty() ? Portfolio.EMPTY : new Portfolio(newConstituents, cash);
    }

    return this;
  }

  public Set<Constituent> getConstituents() {
    return constituents;
  }

  public PortfolioDO toDataObject() {
    Set<ConstituentDO> cdos = Sets.newHashSetWithExpectedSize(this.constituents.size());
    for (Constituent cd : this.constituents) {
      cdos.add(cd.toDataObject());
    }
    return new PortfolioDO(cdos, cash.toDataObject());
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

    public ConstituentDO toDataObject() {
      return new ConstituentDO(this.instrument.toDataObject(), this.number);
    }
  }

  public static class Builder {

    private Map<Instrument, Integer> instruments = Maps.newHashMap();
    private Cash cash = Cash.ZERO;

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

      Portfolio portfolio = new Portfolio(instruments, cash);
      instruments.clear();
      return portfolio;
    }
  }
}
