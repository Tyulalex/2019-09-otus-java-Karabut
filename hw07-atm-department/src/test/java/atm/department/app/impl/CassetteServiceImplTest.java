package atm.department.app.impl;

import atm.department.app.CassetteService;
import atm.department.app.Nominal;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.model.Cassette;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CassetteServiceImplTest extends BaseTest {

    @Test
    void withdrawWhenHasEnoughBanknotes() throws Exception {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        new CassetteServiceImpl(cassette).withdrawNotes(5);
        assertEquals(5, cassette.getQuantity());
    }

    @Test
    void withdrawAllNotesWhenHasEnoughBanknotes() throws Exception {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        new CassetteServiceImpl(cassette).withdrawNotes(10);
        assertEquals(0, cassette.getQuantity());
    }

    @Test
    void withdrawThrowsOutOfBanknoteException() throws Exception {
        Cassette cassette = cassetteWithDefaultNominalAndCapacity(10);
        CassetteService cassetteService = new CassetteServiceImpl(cassette);
        cassetteService.withdrawNotes(10);
        assertThrows(OutOfBanknoteException.class, () -> cassetteService.withdrawNotes(11));
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenEnoughBankNotes() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 10);
        int bankNotesQuantity = new CassetteServiceImpl(cassette).calculateActualBankNoteQuantityPerRequestedSumOfMoney(100);
        assertEquals(2, bankNotesQuantity);
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenNotEnoughBankNotes() {
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N50, 3);
        int bankNotesQuantity = new CassetteServiceImpl(cassette).calculateActualBankNoteQuantityPerRequestedSumOfMoney(200);
        assertEquals(3, bankNotesQuantity);
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenCassetteIsEmpty() {
        Cassette cassette = cassetteWithNominalAndDefaultCapacity(Nominal.N50);
        cassette.setQuantity(0);
        int bankNotesQuantity = new CassetteServiceImpl(cassette).calculateActualBankNoteQuantityPerRequestedSumOfMoney(200);
        assertEquals(0, bankNotesQuantity);
    }

    @Test
    void calculateActualBankNoteQuantityPerRequestedSumOfMoneyWhenNoBanknotesForRequestedSum() {
        Cassette cassette = cassetteWithNominalAndDefaultCapacity(Nominal.N500);
        int bankNotesQuantity = new CassetteServiceImpl(cassette).calculateActualBankNoteQuantityPerRequestedSumOfMoney(200);
        assertEquals(0, bankNotesQuantity);
    }
}
