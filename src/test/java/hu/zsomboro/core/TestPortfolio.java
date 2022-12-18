package hu.zsomboro.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.zsomboro.persistence.entity.ConstituentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;

public class TestPortfolio {

  private Portfolio initial;

  @BeforeEach
  public void init() {
    Instrument i1 = new EquitySecurity("inst1", "INST1", InstrumentType.STOCK);
    Instrument i2 = new FixedIncomeSecurity("inst2", "INST2", InstrumentType.TREASURY_BOND, LocalDate.now(), 2.3d);
    Portfolio.Builder builder = new Portfolio.Builder();
    builder.add(i1, 10);
    builder.add(i2, 5);
    initial = builder.build();
  }

  @Test
  public void testAddNewConstituentToPortfolio() {
    Instrument newInstrument = new EquitySecurity("inst3", "INST3", InstrumentType.EXCHANGE_TRADED_FUND);
    Portfolio mutated = initial.add(newInstrument, 10);
    assertEquals(3, mutated.getConstituents().size());
    assertNotNull(mutated.getConstituent(newInstrument));
    assertEquals(10, mutated.getConstituent(newInstrument).getNumber());
  }

  @Test
  public void testAddExistingConstituentToPortfolio() {
    Instrument newInstrument = new EquitySecurity("inst1", "INST1", InstrumentType.EXCHANGE_TRADED_FUND);
    Portfolio mutated = initial.add(newInstrument, 10);
    assertEquals(2, mutated.getConstituents().size());
    assertNotNull(mutated.getConstituent(newInstrument));
    assertEquals(20, mutated.getConstituent(newInstrument).getNumber());
  }

  @Test
  public void testAddNewConstituentWithWrongNumber() {
    assertThrows(IllegalArgumentException.class, () -> {
      Instrument newInstrument = new EquitySecurity("inst3", "INST3", InstrumentType.EXCHANGE_TRADED_FUND);
      Portfolio mutated = initial.add(newInstrument, -10);
    });
  }

  @Test
  public void testRemoveMissingConstituentFromPortfolio() {
    Instrument newInstrument = new EquitySecurity("inst3", "INST3", InstrumentType.EXCHANGE_TRADED_FUND);
    Portfolio mutated = initial.remove(newInstrument, 10);
    assertEquals(2, mutated.getConstituents().size());
    assertNull(mutated.getConstituent(newInstrument));
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Removed() {
    Instrument newInstrument = new EquitySecurity("inst1", "INST1", InstrumentType.EXCHANGE_TRADED_FUND);
    Portfolio mutated = initial.remove(newInstrument, 10);
    assertEquals(1, mutated.getConstituents().size());
    assertNull(mutated.getConstituent(newInstrument));
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Remains() {
    Instrument newInstrument = new EquitySecurity("inst1", "INST1", InstrumentType.EXCHANGE_TRADED_FUND);
    Portfolio mutated = initial.remove(newInstrument, 5);
    assertEquals(2, mutated.getConstituents().size());
    assertNotNull(mutated.getConstituent(newInstrument));
    assertEquals(5, mutated.getConstituent(newInstrument).getNumber());
  }

  @Test
  public void testConvertPortfolioToDataObject() {
    PortfolioDO pdo = initial.toDataObject();
    for (Portfolio.Constituent constituent : initial.getConstituents()) {
      boolean found = false;
      for (ConstituentDO cdo : pdo.getConstituents()) {
        if (cdo.getInstrument().getIdentifier().equals(constituent.getInstrument().getIdentifier())) {
          assertEquals(constituent.getNumber(), cdo.getNumber());
          assertEquals(constituent.getInstrument().getName(), cdo.getInstrument().getName());
          found = true;
        }
      }
      assertTrue(found);
    }
  }
}
