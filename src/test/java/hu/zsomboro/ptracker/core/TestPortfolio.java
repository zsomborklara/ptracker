package hu.zsomboro.ptracker.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu.zsomboro.ptracker.core.security.EquitySecurity;
import hu.zsomboro.ptracker.core.security.FixedIncomeSecurity;
import hu.zsomboro.ptracker.core.security.Instrument;

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
    Portfolio mutated = initial.withCash(cash);
    assertEquals(2, mutated.getConstituents().size());
    assertEquals(cash, mutated.getCash());
  }

  @Test
  public void testAddNewConstituentToPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst3", "INST3");
    Portfolio mutated = initial.withInstrument(newInstrument, 10);
    assertEquals(3, mutated.getConstituents().size());
    assertNotNull(mutated.getConstituent(newInstrument));
    assertEquals(10, mutated.getConstituent(newInstrument).number());
  }

  @Test
  public void testAddExistingConstituentToPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
    Portfolio mutated = initial.withInstrument(newInstrument, 10);
    assertEquals(2, mutated.getConstituents().size());
    assertNotNull(mutated.getConstituent(newInstrument));
    assertEquals(20, mutated.getConstituent(newInstrument).number());
  }

  @Test
  public void testAddNewConstituentWithWrongNumber() {
    assertThrows(IllegalArgumentException.class, () -> {
      Instrument newInstrument = EquitySecurity.newETF("inst3", "INST3");
      initial.withInstrument(newInstrument, -10);
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
    assertEquals(5, mutated.getConstituent(newInstrument).number());
  }

}
