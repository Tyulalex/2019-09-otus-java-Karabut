package atm.department.app.impl;

import atm.department.app.Atm;
import atm.department.app.Cassettes;
import atm.department.app.Nominal;
import atm.department.exceptions.ExceedMaxSumPerOperationException;
import atm.department.exceptions.OutOfMoneyException;
import atm.department.exceptions.UnsupportedAmountRequestedException;
import atm.department.state.AtmState;
import atm.department.state.CassetteState;
import atm.department.state.CassettesState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyInt;

class AtmImplTest extends BaseTest {

    private static final int MAX_SUM_PER_OPERATION = 200000;

    @Test
    public void atmCallsWithdrawOnce() {
        var cassettes = getCassettesDefaultMock();
        createAtmWithCassettesMock(cassettes).withdrawMoney(5000);
        Mockito.verify(cassettes, Mockito.times(1)).withdrawMoney(5000);
    }

    @Test
    public void withdrawWhenExceedMaxSumPerOperation() {
        var cassettes = getCassettesDefaultMock();
        var atm = createAtmWithCassettesMock(cassettes);

        assertThrows(ExceedMaxSumPerOperationException.class,
                () -> atm.withdrawMoney(MAX_SUM_PER_OPERATION + 100));

        Mockito.verify(cassettes, Mockito.times(0)).withdrawMoney(anyInt());
    }

    @Test
    public void testWithdrawWhenIncorrectSum() {
        var cassettes = getCassettesDefaultMock();
        var atm = createAtmWithCassettesMock(cassettes);
        assertThrows(UnsupportedAmountRequestedException.class,
                () -> atm.withdrawMoney(2345));
        Mockito.verify(cassettes, Mockito.times(0)).withdrawMoney(anyInt());
    }

    @Test
    public void testOutOfMoneyException() {
        var cassettes = getCassettesDefaultMock();
        var atm = createAtmWithCassettesMock(cassettes);
        assertThrows(OutOfMoneyException.class, () -> atm.withdrawMoney(15000));
        Mockito.verify(cassettes, Mockito.times(0)).withdrawMoney(anyInt());
    }

    @Test
    public void testGetTotalSumOfMoneyReturnsSameAsCassettesGetTotal() {
        int sumOfMoney = 80000;
        var cassettes = getCassettesMockWithSumOfMoney(sumOfMoney);
        var atm = createAtmWithCassettesMock(cassettes);
        Assertions.assertEquals(sumOfMoney, atm.getTotalSumOfMoney());
        Mockito.verify(cassettes, Mockito.times(1)).getTotalSumOfMoney();
    }

    @Test
    public void getState() {
        var cassettes = cassettesWithMockGetState();

        Atm atm = createAtmWithCassettesMock(cassettes);

        assertEquals(cassettes.getState(), atm.getState().getCassettesState());
    }

    @Test
    public void restoreState() {
        var cassettes = cassettesWithNominalAndMaxCapacityMock(
                new Nominal[]{Nominal.N50, Nominal.N100},
                new int[]{5, 10});
        Atm atm = createAtmWithCassettesMock(cassettes);
        AtmState atmState = atm.getState();
        int sumOfMoney = atm.getTotalSumOfMoney();
        atm.withdrawMoney(1050);

        assertNotEquals(sumOfMoney, atm.getTotalSumOfMoney());
        assertEquals(0, cassettes.getCassetteByNominal(Nominal.N100).getQuantity());
        assertEquals(4, cassettes.getCassetteByNominal(Nominal.N50).getQuantity());

        atm.restoreState(atmState);

        assertEquals(sumOfMoney, atm.getTotalSumOfMoney());
        assertEquals(10, cassettes.getCassetteByNominal(Nominal.N100).getQuantity());
        assertEquals(5, cassettes.getCassetteByNominal(Nominal.N50).getQuantity());

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
        var atmBuilder = Mockito.mock(AtmBuilder.class);
        Mockito.when(atmBuilder.getCassettes()).thenReturn(
                cassettes == null ? getCassettesDefaultMock() : cassettes);
        Mockito.when(atmBuilder.getMaxSumPerOperation()).thenReturn(MAX_SUM_PER_OPERATION);
        Mockito.when(atmBuilder.getAtmType()).thenReturn(new BuiltInAtm());

        return new AtmImpl(atmBuilder);
    }

    private Cassettes cassettesWithMockGetState() {
        var cassettes = Mockito.mock(Cassettes.class);
        Mockito.when(cassettes.getState()).thenReturn(
                new CassettesState() {
                    @Override
                    public List<CassetteState> getCassettesState() {
                        return List.of(
                                new CassetteState() {
                                    @Override
                                    public int getQuantity() {
                                        return 5;
                                    }

                                    @Override
                                    public Nominal getNominal() {
                                        return Nominal.N1000;
                                    }
                                },
                                new CassetteState() {
                                    @Override
                                    public int getQuantity() {
                                        return 50;
                                    }

                                    @Override
                                    public Nominal getNominal() {
                                        return Nominal.N100;
                                    }
                                }
                        );
                    }
                }
        );
        return cassettes;
    }
}