package atm.emulator.app.impl;

import atm.emulator.app.Cassette;
import atm.emulator.app.Cassettes;
import atm.emulator.app.Nominal;
import atm.emulator.exceptions.OutOfBanknoteException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyInt;

class CassettesImplTest extends BaseTest {

    @Test
    void getTotalSumOfMoney() {
        Cassette cassette1 = cassetteWithGetAmountMockReturn(5);
        Cassette cassette2 = cassetteWithGetAmountMockReturn(10);

        Cassettes cassettes = new CassettesImpl(List.of(cassette1, cassette2));

        assertEquals(15, cassettes.getTotalSumOfMoney());
        Mockito.verify(cassette1, Mockito.times(1)).getAmount();
        Mockito.verify(cassette2, Mockito.times(1)).getAmount();

    }

    @Test
    void performWithdrawTheEldestNominalFirst() {
        Cassettes cassettes = cassettesWithConfiguration(10);
        int initialSumOfMoney = cassettes.getTotalSumOfMoney();

        cassettes.performWithdraw(5000);

        assertEquals(9, cassettes.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(initialSumOfMoney - 5000, cassettes.getTotalSumOfMoney());
    }

    @Test
    void performWithdrawWhenEldestNominalEnded() {
        Cassettes cassettes = cassettesWithConfiguration(10);
        cassettes.getCassetteByNominal(Nominal.N5000).setQuantity(0);
        int initialSumOfMoney = cassettes.getTotalSumOfMoney();

        cassettes.performWithdraw(5250);

        assertEquals(0, cassettes.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(initialSumOfMoney - 5250, cassettes.getTotalSumOfMoney());
        assertEquals(9, cassettes.getCassetteByNominal(Nominal.N200).getQuantity());
        assertEquals(9, cassettes.getCassetteByNominal(Nominal.N50).getQuantity());
    }

    @Test
    void performWithdrawHappensbyMinimumAvailableBanknotes() {
        Cassette cassette1 = cassetteWithGetNominalAndQuantityReturn(Nominal.N5000, 3);
        Cassette cassette2 = cassetteWithGetNominalAndQuantityReturn(Nominal.N1000, 100);
        Cassette cassette3 = cassetteWithGetNominalAndQuantityReturn(Nominal.N200, 20);
        Cassettes cassettes = new CassettesImpl(List.of(cassette1, cassette2, cassette3));

        int sumOfMoneyToWithdraw = 30000;

        Mockito.when(cassette1.calculateActualBankNoteQuantityPerRequestedSumOfMoney(anyInt()))
                .thenReturn(3);
        Mockito.when(cassette2.calculateActualBankNoteQuantityPerRequestedSumOfMoney(anyInt()))
                .thenReturn(15);
        Mockito.when(cassette3.calculateActualBankNoteQuantityPerRequestedSumOfMoney(anyInt()))
                .thenReturn(20);

        cassettes.performWithdraw(sumOfMoneyToWithdraw);

        Mockito.verify(cassette1, Mockito.times(1)).withdraw(3);
        Mockito.verify(cassette2, Mockito.times(1)).withdraw(15);
        Mockito.verify(cassette3, Mockito.times(0)).withdraw(anyInt());

    }

    @Test
    void getCassetteByNominal() {
        Cassettes cassettes = cassettesWithConfiguration(10);
        Cassette cassette = new CassetteImpl(Nominal.N100, () -> 10);
        assertTrue(cassette.equals(cassettes.getCassetteByNominal(Nominal.N100)));
    }

    @Test
    public void testOutOfBanknoteException() {
        Cassette cassette = cassetteWithGetNominalAndQuantityReturn(Nominal.N1000, 100);
        Cassettes cassettes = new CassettesImpl(List.of(cassette));
        assertThrows(OutOfBanknoteException.class, () -> cassettes.performWithdraw(900));
    }

    private Cassettes cassettesWithConfiguration(int maxCapacity) {
        var configuration = cassetteConfigurationMockWithMaxCapacity(maxCapacity);
        return new CassettesImpl(configuration);
    }

    private Cassette cassetteWithGetAmountMockReturn(int amount) {
        Cassette cassette1 = Mockito.mock(Cassette.class);
        Mockito.when(cassette1.getAmount()).thenReturn(amount);

        return cassette1;
    }

    private Cassette cassetteWithGetNominalAndQuantityReturn(Nominal nominal, int quantity) {
        Cassette cassette1 = Mockito.mock(Cassette.class);
        Mockito.when(cassette1.getNominal()).thenReturn(nominal);
        Mockito.when(cassette1.getQuantity()).thenReturn(quantity);

        return cassette1;
    }
}