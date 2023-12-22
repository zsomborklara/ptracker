package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.Cash;
import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.persistence.entity.CashDO;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PriceMapper {
    PriceMapper INSTANCE = Mappers.getMapper( PriceMapper.class );

    @Mapping(target = "value", source = "price")
    @Mapping(target = "priceCurrency", source = "currency")
    Price priceDOToPrice(PriceDO price);

    @InheritInverseConfiguration
    PriceDO priceToPriceDO(Price price);
}
