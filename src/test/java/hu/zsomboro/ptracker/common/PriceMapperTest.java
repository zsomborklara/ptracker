package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.Price;
import hu.zsomboro.ptracker.persistence.entity.PriceDO;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceMapperTest {

    @Test
    public void shouldMapPriceDOToDomain() {
        //given
        PriceDO priceDo = new PriceDO();
        priceDo.setPrice(124.d);
        priceDo.setCurrency("HUF");
        priceDo.setIdentifier("dummyId");
        priceDo.setAsOfDate(LocalDate.now());

        //when
        Price price = PriceMapper.INSTANCE.priceDOToPrice(priceDo);

        //then
        assertThat(price).isNotNull();
        assertThat(price.value()).isCloseTo(124.d, Offset.offset(0.d));
        assertThat(price.priceCurrency()).isEqualTo("HUF");
    }

    @Test
    public void shouldMapPriceToDO() {
        //given
        Price price = new Price(124.d, "HUF");

        //when
        PriceDO priceDO = PriceMapper.INSTANCE.priceToPriceDO(price);

        //then
        assertThat(priceDO).isNotNull();
        assertThat(priceDO.getPrice()).isCloseTo(124.d, Offset.offset(0.d));
        assertThat(priceDO.getCurrency()).isEqualTo("HUF");
        assertThat(priceDO.getAsOfDate()).isNull();
        assertThat(priceDO.getIdentifier()).isNull();
    }
}
