package atm.department.app.impl;

import atm.department.app.AtmService;
import atm.department.app.CassettesService;
import atm.department.exceptions.AtmOperationException;
import atm.department.exceptions.ExceedMaxSumPerOperationException;
import atm.department.exceptions.OutOfMoneyException;
import atm.department.exceptions.UnsupportedAmountRequestedException;
import atm.department.model.Atm;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;

class AtmServiceImplTest extends BaseTest {

    private static final int MAX_SUM_PER_OPERATION = 200000;

    @Test
    public void atmCallsWithdrawOnce() throws AtmOperationException {
        var cassettes = getCassettesDefaultMock();
        var atm = Mockito.mock(Atm.class);
        Mockito.when(atm.getMultiplicityParameter()).thenReturn(50);
        Mockito.when(atm.getMaxSumPerOperation()).thenReturn(MAX_SUM_PER_OPERATION);
        Mockito.when(atm.getAmountOfMoney()).thenReturn(2000000);
        CassettesService cassettesService = Mockito.spy(new CassettesServiceImpl(cassettes));
        Mockito.doNothing().when(cassettesService).withdrawMoney(anyInt());
        AtmService atmService = new AtmServiceImpl(atm, cassettesService);
        atmService.withdrawMoney(5000);
        Mockito.verify(cassettesService, Mockito.times(1)).withdrawMoney(5000);
    }

    @Test
    public void withdrawWhenExceedMaxSumPerOperation() throws AtmOperationException {
        var cassettes = getCassettesDefaultMock();
        var atm = Mockito.mock(Atm.class);
        Mockito.when(atm.getMultiplicityParameter()).thenReturn(50);
        Mockito.when(atm.getMaxSumPerOperation()).thenReturn(MAX_SUM_PER_OPERATION);
        Mockito.when(atm.getAmountOfMoney()).thenReturn(2000000);
        CassettesService cassettesService = Mockito.spy(new CassettesServiceImpl(cassettes));
        AtmService atmService = new AtmServiceImpl(atm, cassettesService);

        assertThrows(ExceedMaxSumPerOperationException.class,
                () -> atmService.withdrawMoney(MAX_SUM_PER_OPERATION + 100));

        Mockito.verify(cassettesService, Mockito.times(0)).withdrawMoney(anyInt());
    }

    @Test
    public void testWithdrawWhenIncorrectSum() throws AtmOperationException {
        var cassettes = getCassettesDefaultMock();
        var atm = Mockito.mock(Atm.class);
        Mockito.when(atm.getMultiplicityParameter()).thenReturn(50);
        Mockito.when(atm.getMaxSumPerOperation()).thenReturn(MAX_SUM_PER_OPERATION);
        Mockito.when(atm.getAmountOfMoney()).thenReturn(200000);
        CassettesService cassettesService = Mockito.spy(new CassettesServiceImpl(cassettes));
        AtmService atmService = new AtmServiceImpl(atm, cassettesService);
        assertThrows(UnsupportedAmountRequestedException.class,
                () -> atmService.withdrawMoney(2345));
        Mockito.verify(cassettesService, Mockito.times(0)).withdrawMoney(anyInt());
    }

    @Test
    public void testOutOfMoneyException() throws AtmOperationException {
        var cassettes = getCassettesDefaultMock();
        var atm = Mockito.mock(Atm.class);
        Mockito.when(atm.getMultiplicityParameter()).thenReturn(50);
        Mockito.when(atm.getMaxSumPerOperation()).thenReturn(MAX_SUM_PER_OPERATION);
        Mockito.when(atm.getAmountOfMoney()).thenReturn(10000);
        CassettesService cassettesService = Mockito.spy(new CassettesServiceImpl(cassettes));
        AtmService atmService = new AtmServiceImpl(atm, cassettesService);

        assertThrows(OutOfMoneyException.class, () -> atmService.withdrawMoney(15000));
        Mockito.verify(cassettesService, Mockito.times(0)).withdrawMoney(anyInt());
    }
}