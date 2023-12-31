package hu.zsomboro.ptracker.core;

import static org.assertj.core.api.Assertions.assertThat;
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
    builder.add(new Cash(1000, "HUF"));
    builder.withId(1);
    builder.withName("Dummy");
    initial = builder.build();
  }

  @Test
  public void testAddCashToPortfolio() {
    Cash cash = new Cash(100, "USD");
    Portfolio mutated = initial.withCash(cash);
    assertThat(mutated.getConstituents()).hasSize(2);
    assertThat(mutated.getCash()).isEqualTo(cash);
  }

  @Test
  public void testAddNewConstituentToPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst3", "INST3");
    Portfolio mutated = initial.withInstrument(newInstrument, 10);
    assertThat(mutated.getConstituents()).hasSize(3);
    assertThat(mutated.getConstituent(newInstrument)).isNotNull();
    assertThat(mutated.getConstituent(newInstrument).number()).isEqualTo(10);
  }

  @Test
  public void testAddExistingConstituentToPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
    Portfolio mutated = initial.withInstrument(newInstrument, 10);
    assertThat(mutated.getConstituents()).hasSize(2);
    assertThat(mutated.getConstituent(newInstrument)).isNotNull();
    assertThat(mutated.getConstituent(newInstrument).number()).isEqualTo(20);
  }

  @Test
  public void testAddNewConstituentWithWrongNumber() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
      initial.withInstrument(newInstrument, -10);
    });
    assertThat(exception.getMessage()).isEqualTo("Cannot add a negative number of instruments");
  }

  @Test
  public void testSubtractConstituentWithWrongNumber() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
      initial.remove(newInstrument, -30);
    });
    assertThat(exception.getMessage()).isEqualTo("Cannot subtract a negative number of instruments");
  }

  @Test
  public void testSubtractConstituentWitMoreThanExists() {
    assertThrows(IllegalStateException.class, () -> {
      Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
      initial.remove(newInstrument, 3000);
    });
  }

  @Test
  public void testRemoveMissingConstituentFromPortfolio() {
    Instrument newInstrument = EquitySecurity.newETF("inst3", "INST3");
    Portfolio mutated = initial.remove(newInstrument, 10);
    assertThat(mutated.getConstituents()).hasSize(2);
    assertThat(mutated.getConstituent(newInstrument)).isNull();
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Removed() {
    Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
    Portfolio mutated = initial.remove(newInstrument, 10);
    assertThat(mutated.getConstituents()).hasSize(1);
    assertThat(mutated.getConstituent(newInstrument)).isNull();
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Remains() {
    Instrument newInstrument = EquitySecurity.newETF("inst1", "INST1");
    Portfolio mutated = initial.remove(newInstrument, 5);
    assertThat(mutated.getConstituents()).hasSize(2);
    assertThat(mutated.getConstituent(newInstrument)).isNotNull();
    assertThat(mutated.getConstituent(newInstrument).number()).isEqualTo(5);
  }

}
