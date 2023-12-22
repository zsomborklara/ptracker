package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.Cash;
import hu.zsomboro.ptracker.persistence.entity.CashDO;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CashMapperTest {

    @Test
    public void shouldMapCashToDO() {
        //given
        Cash cash = new Cash(12.d, "USD");

        //when
        CashDO casDo = CashMapper.INSTANCE.cashToCashDO(cash);

        //then
        assertThat(casDo).isNotNull();
        assertThat(casDo.getAmount()).isCloseTo(12.d, Offset.offset(0.d));
        assertThat(casDo.getCurrency()).isEqualTo("USD");
        assertThat(casDo.getId()).isEqualTo(0L);
    }

    @Test
    public void shouldMapCashDOToDomain() {
        //given
        CashDO cashDo = new CashDO("USD", 12.d);

        //when
        Cash cash = CashMapper.INSTANCE.cashDOToCash(cashDo);

        //then
        assertThat(cash).isNotNull();
        assertThat(cash.amount()).isCloseTo(12.d, Offset.offset(0.d));
        assertThat(cash.currency()).isEqualTo("USD");
    }


}
