package atm.emulator.app.impl;

import atm.emulator.app.Atm;
import atm.emulator.app.Cassettes;
import atm.emulator.exceptions.ExceedMaxSumPerOperationException;
import atm.emulator.exceptions.OutOfMoneyException;
import atm.emulator.exceptions.UnsupportedAmountRequestedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;

final class AtmEmulatorImplTest {

    private static final int MAX_SUM_PER_OPERATION = 200000;

    @Test
    public void atmCallsWithdrawOnce() {
        var cassettes = getCassettesDefaultMock();
        createAtmWithCassettesMock(cassettes).withdrawMoney(5000);
        Mockito.verify(cassettes, Mockito.times(1)).performWithdraw(5000);
    }

    @Test
    public void withdrawWhenExceedMaxSumPerOperation() {
        var cassettes = getCassettesDefaultMock();
        var atm = createAtmWithCassettesMock(cassettes);

        assertThrows(ExceedMaxSumPerOperationException.class,
                () -> atm.withdrawMoney(MAX_SUM_PER_OPERATION + 100));

        Mockito.verify(cassettes, Mockito.times(0)).performWithdraw(anyInt());
    }

    @Test
    public void testWithdrawWhenIncorrectSum() {
        var cassettes = getCassettesDefaultMock();
        var atm = createAtmWithCassettesMock(cassettes);
        assertThrows(UnsupportedAmountRequestedException.class,
                () -> atm.withdrawMoney(2345));
        Mockito.verify(cassettes, Mockito.times(0)).performWithdraw(anyInt());
    }

    @Test
    public void testOutOfMoneyException() {
        var cassettes = getCassettesDefaultMock();
        var atm = createAtmWithCassettesMock(cassettes);
        assertThrows(OutOfMoneyException.class, () -> atm.withdrawMoney(15000));
        Mockito.verify(cassettes, Mockito.times(0)).performWithdraw(anyInt());
    }

    @Test
    public void testGetTotalSumOfMoneyReturnsSameAsCassettesGetTotal() {
        int sumOfMoney = 80000;
        var cassettes = getCassettesMockWithSumOfMoney(sumOfMoney);
        var atm = createAtmWithCassettesMock(cassettes);
        Assertions.assertEquals(sumOfMoney, atm.getTotalSumOfMoney());
        Mockito.verify(cassettes, Mockito.times(1)).getTotalSumOfMoney();
    }


    private Cassettes getCassettesDefaultMock() {
        return getCassettesMockWithSumOfMoney(10000);
    }

    private Cassettes getCassettesMockWithSumOfMoney(int sumOfMoney) {
        var cassettes = Mockito.mock(Cassettes.class);
        Mockito.when(cassettes.getTotalSumOfMoney()).thenReturn(sumOfMoney);

        return cassettes;
    }

    private Atm createAtmWithCassettesMock(Cassettes cassettes) {
        cassettes = cassettes == null ? getCassettesDefaultMock() : cassettes;

        return new AtmEmulatorImpl(() -> MAX_SUM_PER_OPERATION, cassettes);
    }
}
