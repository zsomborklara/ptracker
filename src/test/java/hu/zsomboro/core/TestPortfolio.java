package hu.zsomboro.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.zsomboro.core.security.EquitySecurity;
import hu.zsomboro.core.security.FixedIncomeSecurity;
import hu.zsomboro.core.security.Instrument;
import hu.zsomboro.persistence.entity.ConstituentDO;
import hu.zsomboro.persistence.entity.PortfolioDO;

public class TestPortfolio {

  private Portfolio initial;

  @BeforeEach
  public void init() {
    Instrument i1 = EquitySecurity.newStock("inst1", "INST1");
    Instrument i2 = FixedIncomeSecurity.newTBond("inst2", "INST2", LocalDate.now(), 2.3d);
    Portfolio.Builder builder = new Portfolio.Builder();
    builder.add(i1, 10);
    builder.add(i2, 5);
    initial = builder.build();
  }

  @Test
  public void testAddCashToPortfolio() {
    Cash cash = new Cash(100, "USD");
    Portfolio mutated = initial.add(cash);
    assertEquals(2, mutated.getConstituents().size());
    assertEquals(cash, mutated.getCash());
  }

  @Test
  public void testAddNewConstituentToPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst3", "INST3");
    Portfolio mutated = initial.add(newInstrument, 10);
    assertEquals(3, mutated.getConstituents().size());
    assertNotNull(mutated.getConstituent(newInstrument));
    assertEquals(10, mutated.getConstituent(newInstrument).getNumber());
  }

  @Test
  public void testAddExistingConstituentToPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
    Portfolio mutated = initial.add(newInstrument, 10);
    assertEquals(2, mutated.getConstituents().size());
    assertNotNull(mutated.getConstituent(newInstrument));
    assertEquals(20, mutated.getConstituent(newInstrument).getNumber());
  }

  @Test
  public void testAddNewConstituentWithWrongNumber() {
    assertThrows(IllegalArgumentException.class, () -> {
      Instrument newInstrument = EquitySecurity.newETF("inst3", "INST3");
      Portfolio mutated = initial.add(newInstrument, -10);
    });
  }

  @Test
  public void testRemoveMissingConstituentFromPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst3", "INST3");
    Portfolio mutated = initial.remove(newInstrument, 10);
    assertEquals(2, mutated.getConstituents().size());
    assertNull(mutated.getConstituent(newInstrument));
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Removed() {
    Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
    Portfolio mutated = initial.remove(newInstrument, 10);
    assertEquals(1, mutated.getConstituents().size());
    assertNull(mutated.getConstituent(newInstrument));
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Remains() {
    Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
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
