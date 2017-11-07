package hu.zsomboro.core;

import com.google.common.collect.Sets;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

public class TestPortfolio {

  private Portfolio initial;

  @Before
  public void init() {
    Instrument i1 =  new Instrument("inst1", "INST1", IdentifierType.ISIN);
    Instrument i2 =  new Instrument("inst2", "INST2", IdentifierType.ISIN);
    Portfolio.Builder builder = new Portfolio.Builder();
    builder.add(i1, 10);
    builder.add(i2, 5);
    initial = builder.build();
  }

  @Test
  public void testAddNewConstituentToPortfolio() {
    Instrument newInstrument =  new Instrument("inst3", "INST3", IdentifierType.ISIN);
    Portfolio mutated = initial.add(newInstrument, 10);
    Assert.assertEquals(3, mutated.getConstituents().size());
    Assert.assertNotNull(mutated.getConstituent(newInstrument));
    Assert.assertEquals(10, mutated.getConstituent(newInstrument).getNumber());
  }

  @Test
  public void testAddExistingConstituentToPortfolio() {
    Instrument newInstrument =  new Instrument("inst1", "INST1", IdentifierType.ISIN);
    Portfolio mutated = initial.add(newInstrument, 10);
    Assert.assertEquals(2, mutated.getConstituents().size());
    Assert.assertNotNull(mutated.getConstituent(newInstrument));
    Assert.assertEquals(20, mutated.getConstituent(newInstrument).getNumber());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNewConstituentWithWrongNumber() {
    Instrument newInstrument =  new Instrument("inst3", "INST3", IdentifierType.ISIN);
    Portfolio mutated = initial.add(newInstrument, -10);
  }

  @Test
  public void testRemoveMissingConstituentFromPortfolio() {
    Instrument newInstrument =  new Instrument("inst3", "INST3", IdentifierType.ISIN);
    Portfolio mutated = initial.remove(newInstrument, 10);
    Assert.assertEquals(2, mutated.getConstituents().size());
    Assert.assertNull(mutated.getConstituent(newInstrument));
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Removed() {
    Instrument newInstrument =  new Instrument("inst1", "INST1", IdentifierType.ISIN);
    Portfolio mutated = initial.remove(newInstrument, 10);
    Assert.assertEquals(1, mutated.getConstituents().size());
    Assert.assertNull(mutated.getConstituent(newInstrument));
  }

  @Test
  public void testRemoveExistingConstituentFromPortfolio_Remains() {
    Instrument newInstrument =  new Instrument("inst1", "INST1", IdentifierType.ISIN);
    Portfolio mutated = initial.remove(newInstrument, 5);
    Assert.assertEquals(2, mutated.getConstituents().size());
    Assert.assertNotNull(mutated.getConstituent(newInstrument));
    Assert.assertEquals(5, mutated.getConstituent(newInstrument).getNumber());
  }
}
