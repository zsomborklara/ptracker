package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.Cash;
import hu.zsomboro.ptracker.persistence.entity.CashDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CashMapper {
    CashMapper INSTANCE = Mappers.getMapper( CashMapper.class );

    CashDO cashToCashDO(Cash cash);

    Cash cashDOToCash(CashDO cash);
}
