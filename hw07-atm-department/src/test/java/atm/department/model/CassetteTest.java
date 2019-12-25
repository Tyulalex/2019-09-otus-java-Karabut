package atm.department.model;

import atm.department.app.Nominal;
import atm.department.app.impl.BaseTest;
import atm.department.model.state.CassetteState;
import atm.department.model.state.Memento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CassetteTest extends BaseTest {

    @Test
    void getNominal() {
        Cassette cassette = cassetteWithNominalAndDefaultCapacity(DEFAULT_NOMINAL);
        assertEquals(DEFAULT_NOMINAL, cassette.getNominal());
    }

    @Test
    void getQuantity() {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(MAX_NOTE_CAPACITY);
        assertEquals(MAX_NOTE_CAPACITY, cassette.getQuantity());
    }

    @Test
    void getAmount() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        assertEquals(500, cassette.getAmountOfMoney());
    }

    @Test
    void equalsObjectSame() {
        Cassette cassette1 = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        Cassette cassette2 = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        assertTrue(cassette1.equals(cassette2));
        assertTrue(cassette2.equals(cassette1));
        assertTrue(cassette2.equals(cassette2));
    }

    @Test
    void notEqualsObjectNotSame() {
        Cassette cassette1 = cassetteWithNominalAndCapacity(Nominal.N50, 11);
        Cassette cassette2 = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        Cassette cassette3 = cassetteWithNominalAndCapacity(Nominal.N500, 10);
        assertFalse(cassette1.equals(cassette2));
        assertFalse(cassette1.equals(null));
        assertFalse(cassette2.equals(cassette3));
    }

    @Test
    void toStringCheck() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        assertEquals("Cassette{maxNoteCapacity=10, nominal=Nominal{value=50}, quantity=10}", cassette.toString());
    }

    @Test
    void compareCheck() {
        Cassette cassette1 = cassetteWithNominalAndDefaultCapacity(Nominal.N100);
        Cassette cassette2 = cassetteWithNominalAndDefaultCapacity(Nominal.N100);
        Cassette cassette3 = cassetteWithNominalAndDefaultCapacity(Nominal.N200);
        assertEquals(0, cassette1.compareTo(cassette2));
        assertEquals(1, cassette3.compareTo(cassette1));
        assertEquals(-1, cassette1.compareTo(cassette3));
    }

    @Test
    void getState() {
        Cassette cassetteService = cassetteWithNominalAndCapacity(Nominal.N50, 100);
        CassetteState cassetteState = cassetteService.getState();
        assertEquals(cassetteService.getNominal(), cassetteState.getNominal());
        assertEquals(cassetteService.getQuantity(), cassetteState.getQuantity());
    }

    @Test
    void restoreState() {
        int initialQuantity = 50;
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N200, initialQuantity);
        Memento memento = cassette.saveState();
        cassette.setQuantity(0);

        cassette.restoreState(memento);

        assertEquals(initialQuantity, cassette.getQuantity());
        assertEquals(Nominal.N200, cassette.getNominal());
    }

}