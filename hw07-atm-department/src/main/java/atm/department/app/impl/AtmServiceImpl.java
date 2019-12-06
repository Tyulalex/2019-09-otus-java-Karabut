package atm.department.app.impl;

import atm.department.app.AtmService;
import atm.department.app.CassettesService;
import atm.department.exceptions.AtmOperationException;
import atm.department.exceptions.ExceedMaxSumPerOperationException;
import atm.department.exceptions.OutOfMoneyException;
import atm.department.exceptions.UnsupportedAmountRequestedException;
import atm.department.model.Atm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class AtmServiceImpl implements AtmService {

    private Atm atm;
    private CassettesService cassettesService;

    public AtmServiceImpl(Atm atm) {
        this(atm, new CassettesServiceImpl(atm.getCassettesCollection()));
    }

    public AtmServiceImpl(Atm atm, CassettesService cassettesService) {
        this.atm = atm;
        this.cassettesService = cassettesService;
    }

    public void withdrawMoney(int sumOfMoney) throws AtmOperationException {
        log.debug("Total sum of money before withdraw:" + this.atm.getAmountOfMoney());
        log.debug(String.format("Withdraw %d", sumOfMoney));
        checkSumOfMoneyCanBeGiven(sumOfMoney);
        this.cassettesService.withdrawMoney(sumOfMoney);
        log.debug("Total sum of money after withdraw:" + this.atm.getAmountOfMoney());
    }

    private void checkSumOfMoneyCanBeGiven(int sumOfMoney) throws OutOfMoneyException,
            UnsupportedAmountRequestedException, ExceedMaxSumPerOperationException {
        checkRequestedAmountNotExceedsLimit(sumOfMoney);
        checkEnoughMoneyLeft(sumOfMoney);
        checkRequestedAmountMultipleExpected(sumOfMoney);
    }

    private void checkEnoughMoneyLeft(int requestedAmount) throws OutOfMoneyException {
        if (this.atm.getAmountOfMoney() - requestedAmount < 0) {
            throw new OutOfMoneyException();
        }
    }

    private void checkRequestedAmountMultipleExpected(int requestedAmount) throws UnsupportedAmountRequestedException {
        var multiplicity = this.atm.getMultiplicityParameter();
        if (requestedAmount <= 0 || requestedAmount % multiplicity != 0) {
            throw new UnsupportedAmountRequestedException(multiplicity);
        }
    }

    private void checkRequestedAmountNotExceedsLimit(int requestedAmount) throws ExceedMaxSumPerOperationException {
        if (requestedAmount > this.atm.getMaxSumPerOperation()) {
            throw new ExceedMaxSumPerOperationException(this.atm.getMaxSumPerOperation());
        }
    }
}
