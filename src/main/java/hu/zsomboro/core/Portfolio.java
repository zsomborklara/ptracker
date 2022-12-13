package hu.zsomboro.core;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import hu.zsomboro.persistence.entity.ConstituentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;

public class Portfolio {

  public static final Portfolio EMPTY = new Portfolio();

  private final Set<Constituent> constituents;

  public Portfolio(Set<Constituent> constituents) {
    this.constituents = ImmutableSet.copyOf(constituents);
  }

  private Portfolio() {
    constituents = ImmutableSet.of();
  }

  public Portfolio empty() {
    return EMPTY;
  }

  public Constituent getConstituent(Instrument instrument) {
    return Iterables.find(constituents, c -> c.instrument.equals(instrument), null);
  }

  public Portfolio add(final Instrument instrument, int number) {

    final ImmutableSet.Builder<Constituent> builder = ImmutableSet.<Constituent>builder();

    Constituent existing = getConstituent(instrument);

    if (existing != null) {
      builder.addAll(filterExisting(existing));
      builder.add(existing.add(number));
    } else {
      builder.addAll(constituents);
      builder.add(new Constituent(number, instrument));
    }

    return new Portfolio(builder.build());
  }

  public Portfolio remove(Instrument instrument, int number) {

    final ImmutableSet.Builder<Constituent> builder = ImmutableSet.<Constituent>builder();

    Constituent existing = getConstituent(instrument);

    if (existing != null) {
      builder.addAll(filterExisting(existing));
      if (existing.number != number) {
        builder.add(existing.substract(number));
      }
    } else {
      builder.addAll(constituents);
    }
    return new Portfolio(builder.build());
  }

  public Set<Constituent> getConstituents() {
    return constituents;
  }

  public PortfolioDO toDataObject() {
    Set<ConstituentDO> cdos = Sets.newHashSetWithExpectedSize(this.constituents.size());
    for (Constituent cd : this.constituents) {
      cdos.add(cd.toDataObject());
    }
    return new PortfolioDO(cdos);
  }

  private FluentIterable<Constituent> filterExisting(Constituent existing) {
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
      Preconditions.checkState(this.number > number);
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

    private Portfolio interimPortfolio = Portfolio.EMPTY;

    public Builder add(Instrument instrument, int number) {
      interimPortfolio = interimPortfolio.add(instrument, number);
      return this;
    }

    public Portfolio build() {
      Portfolio toReturn = interimPortfolio;
      interimPortfolio = Portfolio.EMPTY;
      return toReturn;
    }
  }
}
