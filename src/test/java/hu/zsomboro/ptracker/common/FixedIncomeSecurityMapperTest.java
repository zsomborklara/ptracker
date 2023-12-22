package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;
import hu.zsomboro.ptracker.core.security.InstrumentType;
import hu.zsomboro.ptracker.persistence.entity.FixedIncomeSecurityDO;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class FixedIncomeSecurityMapperTest {

    @Test
    public void shouldMapFixedIncomeSecurityToDO() {
        //given
        FixedIncomeSecurity fixedIncomeSecurity = FixedIncomeSecurity.newTBond("PEMAP", "Ticker", LocalDate.now(), 10.3d);

        //when
        FixedIncomeSecurityDO fixedIncomeSecurityDo = FixedIncomeSecurityMapper.INSTANCE.fixedIncomeSecurityToFixedIncomeSecurityDO(fixedIncomeSecurity);

        //then
        assertThat(fixedIncomeSecurityDo).isNotNull();
        assertThat(fixedIncomeSecurityDo.getName()).isEqualTo("PEMAP");
        assertThat(fixedIncomeSecurityDo.getInstrumentId()).isEqualTo("Ticker");
        assertThat(fixedIncomeSecurityDo.getMaturity()).isEqualTo(LocalDate.now());
        assertThat(fixedIncomeSecurityDo.getInterestRate()).isCloseTo(10.3d, Offset.offset(0.0001));
        assertThat(fixedIncomeSecurityDo.getInstrumentType()).isEqualTo(InstrumentType.TREASURY_BOND.name());
    }

    @Test
    public void shouldMapFixedIncomeSecurityDOToDomain() {
        //given
        FixedIncomeSecurityDO fixedIncomeSecurityDo =
                new FixedIncomeSecurityDO("Deposit", "DepositId", InstrumentType.DEPOSIT.name(), LocalDate.now(), 10.3d);

        //when
        FixedIncomeSecurity fixedIncomeSecurity = FixedIncomeSecurityMapper.INSTANCE.fixedIncomeSecurityDOToFixedIncomeSecurity(fixedIncomeSecurityDo);

        //then
        assertThat(fixedIncomeSecurity).isNotNull();
        assertThat(fixedIncomeSecurity.getInstrumentId()).isEqualTo("DepositId");
        assertThat(fixedIncomeSecurity.getName()).isEqualTo("Deposit");
        assertThat(fixedIncomeSecurity.getInstrumentType()).isSameAs(InstrumentType.DEPOSIT);
        assertThat(fixedIncomeSecurity.getMaturity()).isEqualTo(LocalDate.now());
        assertThat(fixedIncomeSecurity.getInterestRate()).isCloseTo(10.3d, Offset.offset(0.0001));
    }


}
