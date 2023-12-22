package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.core.security.InstrumentType;
import hu.zsomboro.ptracker.persistence.entity.EquitySecurityDO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EquitySecurityMapperTest {

    @Test
    public void shouldMapEquitySecurityToDO() {
        //given
        EquitySecurity equitySecurity = EquitySecurity.newStock("IBM", "IBMTicker");

        //when
        EquitySecurityDO equitySecurityDo = EquitySecurityMapper.INSTANCE.equitySecurityToEquitySecurityDO(equitySecurity);

        //then
        assertThat(equitySecurityDo).isNotNull();
        assertThat(equitySecurityDo.getName()).isEqualTo("IBM");
        assertThat(equitySecurityDo.getInstrumentId()).isEqualTo("IBMTicker");
        assertThat(equitySecurityDo.getInstrumentType()).isEqualTo(InstrumentType.STOCK.name());
    }

    @Test
    public void shouldMapEquitySecurityDOToDomain() {
        //given
        EquitySecurityDO equitySecurityDo = new EquitySecurityDO("SP500", "ETFTicker", InstrumentType.EXCHANGE_TRADED_FUND.name());

        //when
        EquitySecurity equitySecurity = EquitySecurityMapper.INSTANCE.equitySecurityDOToEquitySecurity(equitySecurityDo);

        //then
        assertThat(equitySecurity).isNotNull();
        assertThat(equitySecurity.getInstrumentId()).isEqualTo("ETFTicker");
        assertThat(equitySecurity.getName()).isEqualTo("SP500");
        assertThat(equitySecurity.getInstrumentType()).isSameAs(InstrumentType.EXCHANGE_TRADED_FUND);
    }


}
