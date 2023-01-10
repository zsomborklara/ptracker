package hu.zsomboro.common;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import hu.zsomboro.core.Cash;
import hu.zsomboro.core.Portfolio;
import hu.zsomboro.core.Portfolio.Builder;
import hu.zsomboro.core.security.EquitySecurity;
import hu.zsomboro.core.security.FixedIncomeSecurity;
import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.core.security.InstrumentType;
import hu.zsomboro.persistence.entity.CashDO;
import hu.zsomboro.persistence.entity.ConstituentDO;
import hu.zsomboro.persistence.entity.EquitySecurityDO;
import hu.zsomboro.persistence.entity.FixedIncomeSecurityDO;
import hu.zsomboro.persistence.entity.InstrumentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;

@Component
public class CoreToPersistenceMapper extends ModelMapper {

  public CoreToPersistenceMapper() {
    super();
    Converter<Cash, CashDO> cashToDoConverter = mappingContext -> {
      Cash cash = mappingContext.getSource();
      return new CashDO(cash.currency(), cash.amount());
    };

    this.typeMap(Portfolio.class, PortfolioDO.class)
        .addMappings(mapper -> mapper.using(cashToDoConverter).map(Portfolio::getCash, PortfolioDO::setCash));

    this.createTypeMap(Instrument.class, InstrumentDO.class).setConverter(mappingContext -> {
      Instrument source = mappingContext.getSource();

      switch (source.getInstrumentType()) {
      case STOCK:
      case EXCHANGE_TRADED_FUND:
        return this.map(source, EquitySecurityDO.class);
      case DEPOSIT:
      case PENSION_FUND:
      case TREASURY_BOND:
        return this.map(source, FixedIncomeSecurityDO.class);
      default:
        throw new IllegalArgumentException("Unexpected value: " + source.getInstrumentType());
      }

    });

    this.createTypeMap(EquitySecurityDO.class, Instrument.class).setConverter(mappingContext -> {
      EquitySecurityDO source = mappingContext.getSource();
      return this.map(source, EquitySecurity.class);
    });
    this.createTypeMap(FixedIncomeSecurityDO.class, Instrument.class).setConverter(mappingContext -> {
      FixedIncomeSecurityDO source = mappingContext.getSource();
      return this.map(source, FixedIncomeSecurity.class);
    });

    this.createTypeMap(EquitySecurityDO.class, EquitySecurity.class).setConverter(mappingContext -> {
      EquitySecurityDO source = mappingContext.getSource();

      return (EquitySecurity) InstrumentType.valueOf(source.getInstrumentType()).create(source.getName(),
          source.getIdentifier(), null, 0.d);

    });

    this.createTypeMap(FixedIncomeSecurityDO.class, FixedIncomeSecurity.class).setConverter(mappingContext -> {
      FixedIncomeSecurityDO source = mappingContext.getSource();

      return (FixedIncomeSecurity) InstrumentType.valueOf(source.getInstrumentType()).create(source.getName(),
          source.getIdentifier(), source.getMaturity(), source.getInterestRate());

    });
    this.createTypeMap(PortfolioDO.class, Portfolio.class).setConverter(mappingContext -> {
      PortfolioDO source = mappingContext.getSource();

      Portfolio.Builder builder = new Builder();
      builder.add(new Cash(source.getCash().getAmount(), source.getCash().getCurrency()));
      for (ConstituentDO constituentDO : source.getConstituents()) {
        builder.add(this.map(constituentDO.getInstrument(), Instrument.class), constituentDO.getNumber());
      }
      builder.withName(source.getName());
      return builder.build();
    });
  }

}
