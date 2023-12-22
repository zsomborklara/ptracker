package hu.zsomboro.ptracker.common;

import hu.zsomboro.ptracker.core.Cash;
import hu.zsomboro.ptracker.core.Portfolio;
import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;
import hu.zsomboro.ptracker.persistence.entity.CashDO;
import hu.zsomboro.ptracker.persistence.entity.ConstituentDO;
import hu.zsomboro.ptracker.persistence.entity.EquitySecurityDO;
import hu.zsomboro.ptracker.persistence.entity.FixedIncomeSecurityDO;
import hu.zsomboro.ptracker.persistence.entity.PortfolioDO;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PortfolioMapperTest {

    @Test
    public void shouldMapPortfolioToDO() {
        //given
        String firstId = "1112233DD";
        EquitySecurity instrument = EquitySecurity.newStock("sec", firstId);
        String secondId = "1112233FF";
        FixedIncomeSecurity instrument2 = FixedIncomeSecurity.newTBond("tbond", secondId, LocalDate.now(), 12.3d);
        Portfolio portfolio = new Portfolio(
                Set.of(new Portfolio.Constituent(1, instrument), new Portfolio.Constituent(3, instrument2)),
                new Cash(123.d, "HUF"), "Dummy", 1);

        //when
        PortfolioDO portfolioDo = PortfolioMapper.INSTANCE.portfolioToPortfolioDO(portfolio);

        //then
        assertThat(portfolioDo).isNotNull();
        assertThat(portfolioDo.getId()).isEqualTo(1);
        assertThat(portfolioDo.getName()).isEqualTo("Dummy");
        assertThat(portfolioDo.getCash().getCurrency()).isEqualTo(portfolio.getCash().currency());
        assertThat(portfolioDo.getCash().getAmount()).isCloseTo(portfolio.getCash().amount(), Offset.offset(0.d));
        Iterator<ConstituentDO> doConstituentIterator = portfolioDo.getConstituents().iterator();
        ConstituentDO first = null;
        ConstituentDO second = null;
        while (doConstituentIterator.hasNext()) {
            ConstituentDO next = doConstituentIterator.next();
            if (next.getInstrument().getInstrumentId().equals(firstId)) {
                first = next;
            } else if (next.getInstrument().getInstrumentId().equals(secondId)) {
                second = next;
            }
        }

        assertThat(portfolio.getConstituent(instrument).number()).isEqualTo(first.getNumber());
        assertThat(portfolio.getConstituent(instrument).instrument().getInstrumentId())
                .isEqualTo(first.getInstrument().getInstrumentId());
        assertThat(portfolio.getConstituent(instrument).instrument().getInstrumentType().toString())
                .isEqualTo(first.getInstrument().getInstrumentType());
        assertThat(first.getPortfolioDO()).isSameAs(portfolioDo);
        assertThat(first.getId().getPortfolioId()).isEqualTo(portfolioDo.getId());
        assertThat(first.getId().getInstrumentId()).isEqualTo(portfolio.getConstituent(instrument).instrument().getInstrumentId());

        assertThat(portfolio.getConstituent(instrument2).number()).isEqualTo(second.getNumber());
        assertThat(portfolio.getConstituent(instrument2).instrument().getInstrumentId())
                .isEqualTo(second.getInstrument().getInstrumentId());
        assertThat(portfolio.getConstituent(instrument2).instrument().getInstrumentType().toString())
                .isEqualTo(second.getInstrument().getInstrumentType());
        assertThat(((FixedIncomeSecurity) portfolio.getConstituent(instrument2).instrument()).getInterestRate())
                .isEqualTo(((FixedIncomeSecurityDO) second.getInstrument()).getInterestRate());
        assertThat(((FixedIncomeSecurity) portfolio.getConstituent(instrument2).instrument()).getMaturity())
                .isEqualTo(((FixedIncomeSecurityDO) second.getInstrument()).getMaturity());
        assertThat(second.getPortfolioDO()).isSameAs(portfolioDo);
        assertThat(second.getId().getPortfolioId()).isEqualTo(portfolioDo.getId());
        assertThat(second.getId().getInstrumentId()).isEqualTo(portfolio.getConstituent(instrument2).instrument().getInstrumentId());
    }

    @Test
    public void shouldMapPortfolioDOToCore() {

        EquitySecurityDO instrumentDTO = new EquitySecurityDO();
        String firstId = "abc1122";
        instrumentDTO.setInstrumentId(firstId);
        instrumentDTO.setInstrumentType("STOCK");
        instrumentDTO.setName("Dummy");

        FixedIncomeSecurityDO instrumentDTO2 = new FixedIncomeSecurityDO();
        String secondId = "cde33222";
        instrumentDTO2.setInstrumentId(secondId);
        instrumentDTO2.setInstrumentType("PENSION_FUND");
        instrumentDTO2.setInterestRate(123.5d);
        instrumentDTO2.setMaturity(LocalDate.now());
        instrumentDTO2.setName("Dummy2");

        PortfolioDO portfolioDTO = new PortfolioDO();
        portfolioDTO.setCash(new CashDO("EUR", 12233.d));
        portfolioDTO.setName("DummyPortfolio");
        portfolioDTO.setConstituents(Set.of(new ConstituentDO(instrumentDTO, 100, portfolioDTO), new ConstituentDO(instrumentDTO2, 200, portfolioDTO)));

        Portfolio portfolio = PortfolioMapper.INSTANCE.portfolioDOToPortfolio(portfolioDTO);

        assertThat(portfolio.getName()).isEqualTo(portfolioDTO.getName());
        assertThat(portfolio.getCash().currency()).isEqualTo(portfolioDTO.getCash().getCurrency());
        assertThat(portfolio.getCash().amount()).isEqualTo(portfolioDTO.getCash().getAmount());
        Iterator<Portfolio.Constituent> constituentIterator = portfolio.getConstituents().iterator();
        Portfolio.Constituent first = null;
        Portfolio.Constituent second = null;
        while (constituentIterator.hasNext()) {
            Portfolio.Constituent next = constituentIterator.next();
            if (next.instrument().getInstrumentId().equals(firstId)) {
                first = next;
            } else if (next.instrument().getInstrumentId().equals(secondId)) {
                second = next;
            }
        }

        Iterator<ConstituentDO> doConstituentIterator = portfolioDTO.getConstituents().iterator();
        ConstituentDO firstDO = null;
        ConstituentDO secondDO = null;
        while (doConstituentIterator.hasNext()) {
            ConstituentDO next = doConstituentIterator.next();
            if (next.getInstrument().getInstrumentId().equals(firstId)) {
                firstDO = next;
            } else if (next.getInstrument().getInstrumentId().equals(secondId)) {
                secondDO = next;
            }
        }

        assertThat(first.number()).isEqualTo(firstDO.getNumber());
        assertThat(first.instrument().getInstrumentId()).isEqualTo(firstDO.getInstrument().getInstrumentId());
        assertThat(first.instrument().getInstrumentType().toString()).isEqualTo(firstDO.getInstrument().getInstrumentType());

        assertThat(second.number()).isEqualTo(secondDO.getNumber());
        assertThat(second.instrument().getInstrumentId()).isEqualTo(secondDO.getInstrument().getInstrumentId());
        assertThat(second.instrument().getInstrumentType().toString()).isEqualTo(secondDO.getInstrument().getInstrumentType());
        assertThat(((FixedIncomeSecurity) second.instrument()).getInterestRate())
                .isEqualTo(((FixedIncomeSecurityDO) secondDO.getInstrument()).getInterestRate());
        assertThat(((FixedIncomeSecurity) second.instrument()).getMaturity())
                .isEqualTo(((FixedIncomeSecurityDO) secondDO.getInstrument()).getMaturity());
    }


}
