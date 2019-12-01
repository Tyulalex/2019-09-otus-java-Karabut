package atm.emulator.app.impl;

import atm.emulator.app.Cassette;
import atm.emulator.app.Nominal;
import atm.emulator.exceptions.OutOfBanknoteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CassetteImplTest extends BaseTest {

    private static final int MAX_NOTE_CAPACITY = 100;
    private static final Nominal DEFAULT_NOMINAL = Nominal.N5000;

    @Test
    void withdrawWhenHasEnoughBanknotes() {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        cassette.withdraw(5);
        assertEquals(5, cassette.getQuantity());
    }

    @Test
    void withdrawAllNotesWhenHasEnoughBanknotes() {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        cassette.withdraw(10);
        assertEquals(0, cassette.getQuantity());
    }

    @Test
    void withdrawThrowsOutOfBanknoteException() {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        cassette.withdraw(10);
        assertThrows(OutOfBanknoteException.class, () -> cassette.withdraw(11));
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenEnoughBankNotes() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 10); //500
        int bankNotesQuantity = cassette.calculateActualBankNoteQuantityPerRequestedSumOfMoney(100);
        assertEquals(2, bankNotesQuantity);
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenNotEnoughBankNotes() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 3); //500
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

    private Cassette cassetteWithNominalAndCapacity(Nominal nominal,
                                                    int maxCapacity) {
        var configuration = cassetteConfigurationMockWithMaxCapacity(maxCapacity);

        return new CassetteImpl(nominal, configuration);
    }

    private Cassette cassetteWithDefaultNominalAndCapacity(int maxCapacity) {
        return cassetteWithNominalAndCapacity(DEFAULT_NOMINAL, maxCapacity);
    }

    private Cassette cassetteWithNominalAndDefaultCapacity(Nominal nominal) {
        return cassetteWithNominalAndCapacity(nominal, MAX_NOTE_CAPACITY);
    }

    private Cassette cassetteWithDefault() {
        return cassetteWithNominalAndCapacity(DEFAULT_NOMINAL, MAX_NOTE_CAPACITY);
    }
}