package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.core.security.InstrumentType;
import hu.zsomboro.ptracker.persistence.entity.EquitySecurityDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquitySecurityMapper {
    EquitySecurityMapper INSTANCE = Mappers.getMapper( EquitySecurityMapper.class );

    EquitySecurityDO equitySecurityToEquitySecurityDO(EquitySecurity security);

    default EquitySecurity equitySecurityDOToEquitySecurity(EquitySecurityDO security) {
        return (EquitySecurity) InstrumentType.valueOf(security.getInstrumentType()).create(security.getName(),
                security.getInstrumentId());
    }

}
