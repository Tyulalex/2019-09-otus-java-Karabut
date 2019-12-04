package atm.department.app.impl;

import atm.department.app.Cassette;
import atm.department.app.Nominal;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.state.CassetteState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CassetteImplTest extends BaseTest {

    private static final int MAX_NOTE_CAPACITY = 100;
    private static final Nominal DEFAULT_NOMINAL = Nominal.N5000;

    @Test
    void withdrawWhenHasEnoughBanknotes() {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        cassette.withdrawNotes(5);
        assertEquals(5, cassette.getQuantity());
    }

    @Test
    void withdrawAllNotesWhenHasEnoughBanknotes() {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        cassette.withdrawNotes(10);
        assertEquals(0, cassette.getQuantity());
    }

    @Test
    void withdrawThrowsOutOfBanknoteException() {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        cassette.withdrawNotes(10);
        assertThrows(OutOfBanknoteException.class, () -> cassette.withdrawNotes(11));
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenEnoughBankNotes() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        int bankNotesQuantity = cassette.calculateActualBankNoteQuantityPerRequestedSumOfMoney(100);
        assertEquals(2, bankNotesQuantity);
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenNotEnoughBankNotes() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 3);
        int bankNotesQuantity = cassette.calculateActualBankNoteQuantityPerRequestedSumOfMoney(200);
        assertEquals(3, bankNotesQuantity);
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenCassetteIsEmpty() {
        Cassette cassette = cassetteWithNominalAndDefaultCapacity(Nominal.N50);
        cassette.setQuantity(0);
        int bankNotesQuantity = cassette.calculateActualBankNoteQuantityPerRequestedSumOfMoney(200);
        assertEquals(0, bankNotesQuantity);
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenNoBanknotesForRequestedSum() {
        Cassette cassette = cassetteWithNominalAndDefaultCapacity(Nominal.N500);
        int bankNotesQuantity = cassette.calculateActualBankNoteQuantityPerRequestedSumOfMoney(200);
        assertEquals(0, bankNotesQuantity);
    }


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
        assertEquals(500, cassette.getAmount());
    }

    @Test
    void equalsObjectSame() {
        Cassette cassette1 = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        Cassette cassette2 = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        assertTrue(cassette1.equals(cassette2));
        assertTrue(cassette1.equals(cassette1));
    }

    @Test
    void notEqualsObjectNotSame() {
        Cassette cassette1 = cassetteWithNominalAndCapacity(Nominal.N50, 11);
        Cassette cassette2 = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        assertFalse(cassette1.equals(cassette2));
        assertFalse(cassette1.equals(null));
    }

    @Test
    void toStringCheck() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        assertEquals("Cassette{nominal=Nominal{value=50}, quantity=10}", cassette.toString());
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
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 100);
        CassetteState cassetteState = cassette.getState();
        assertEquals(cassette.getNominal(), cassetteState.getNominal());
        assertEquals(cassette.getQuantity(), cassetteState.getQuantity());
    }

    @Test
    void restoreState() {
        int initialQuantity = 50;
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N200, initialQuantity);
        CassetteState cassetteState = cassette.getState();
        cassette.withdrawNotes(10);

        cassette.restoreState(cassetteState);

        assertEquals(initialQuantity, cassette.getQuantity());
        assertEquals(Nominal.N200, cassette.getNominal());
    }

    private Cassette cassetteWithDefaultNominalAndCapacity(int maxCapacity) {
        return cassetteWithNominalAndCapacity(DEFAULT_NOMINAL, maxCapacity);
    }

    private Cassette cassetteWithNominalAndDefaultCapacity(Nominal nominal) {
        return cassetteWithNominalAndCapacity(nominal, MAX_NOTE_CAPACITY);
    }
}
