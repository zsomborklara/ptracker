package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;
import hu.zsomboro.ptracker.core.security.Instrument;
import hu.zsomboro.ptracker.persistence.entity.ConstituentId;
import hu.zsomboro.ptracker.persistence.entity.EquitySecurityDO;
import hu.zsomboro.ptracker.persistence.entity.FixedIncomeSecurityDO;
import hu.zsomboro.ptracker.persistence.entity.InstrumentDO;
import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.SubclassMapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { CashMapper.class, EquitySecurityMapper.class, FixedIncomeSecurityMapper.class})
public interface PortfolioMapper {
    PortfolioMapper INSTANCE = Mappers.getMapper( PortfolioMapper.class );

    PortfolioDO portfolioToPortfolioDO(Portfolio portfolio);

    Portfolio portfolioDOToPortfolio(PortfolioDO portfolio);

    @SubclassMapping(source = FixedIncomeSecurity.class, target = FixedIncomeSecurityDO.class)
    @SubclassMapping(source = EquitySecurity.class, target = EquitySecurityDO.class)
    InstrumentDO toDO(Instrument source);

    @SubclassMapping(source = FixedIncomeSecurityDO.class, target = FixedIncomeSecurity.class)
    @SubclassMapping(source = EquitySecurityDO.class, target = EquitySecurity.class)
    Instrument toCore(InstrumentDO source);

    @AfterMapping
    default void updateConstituentDOId(@MappingTarget PortfolioDO portfolioDO) {
        portfolioDO.getConstituents().forEach(c -> {
            c.setPortfolioDO(portfolioDO);
            c.setId(new ConstituentId(portfolioDO.getId(), c.getInstrument().getInstrumentId()));
        });
    }

    default Instrument createInstrument() {
        throw new IllegalStateException("Unsupported instrument mapping");
    }

}
