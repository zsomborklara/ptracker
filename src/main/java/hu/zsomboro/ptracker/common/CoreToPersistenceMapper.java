package hu.zsomboro.ptracker.common;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import hu.zsomboro.core.security.EquitySecurity;
import hu.zsomboro.core.security.FixedIncomeSecurity;
import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.core.security.InstrumentType;
import hu.zsomboro.ptracker.core.Cash;
import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.Portfolio.Builder;
import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.persistence.entity.CashDO;
import hu.zsomboro.ptracker.persistence.entity.ConstituentDO;
import hu.zsomboro.ptracker.persistence.entity.EquitySecurityDO;
import hu.zsomboro.ptracker.persistence.entity.FixedIncomeSecurityDO;
import hu.zsomboro.ptracker.persistence.entity.InstrumentDO;
import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;

@Component
public class CoreToPersistenceMapper extends ModelMapper {

  public CoreToPersistenceMapper() {
    super();

    Converter<Price, PriceDO> priceToDoConverter = mappingContext -> {
      Price price = mappingContext.getSource();
      PriceDO priceDO = new PriceDO();
      priceDO.setCurrency(price.priceCurrency());
      priceDO.setPrice(price.value());
      return priceDO;
    };

    this.createTypeMap(Price.class, PriceDO.class).setConverter(priceToDoConverter);

    Converter<PriceDO, Price> priceDoToCoreConverter = mappingContext -> {
      PriceDO priceDO = mappingContext.getSource();
      return new Price(priceDO.getPrice(), priceDO.getCurrency());
    };

    this.createTypeMap(PriceDO.class, Price.class).setConverter(priceDoToCoreConverter);

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
