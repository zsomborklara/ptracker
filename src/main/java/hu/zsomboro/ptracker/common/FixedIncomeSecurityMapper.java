package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;
import hu.zsomboro.ptracker.core.security.InstrumentType;
import hu.zsomboro.ptracker.persistence.entity.FixedIncomeSecurityDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FixedIncomeSecurityMapper {
    FixedIncomeSecurityMapper INSTANCE = Mappers.getMapper( FixedIncomeSecurityMapper.class );

    FixedIncomeSecurityDO fixedIncomeSecurityToFixedIncomeSecurityDO(FixedIncomeSecurity security);

    default FixedIncomeSecurity fixedIncomeSecurityDOToFixedIncomeSecurity(FixedIncomeSecurityDO security) {
        return (FixedIncomeSecurity) InstrumentType.valueOf(security.getInstrumentType()).createWithInterest(security.getName(),
                security.getInstrumentId(), security.getMaturity(), security.getInterestRate());
    }

}
