package atm.department.app.impl;

import atm.department.app.Atm;
import atm.department.app.AtmLocationType;
import atm.department.app.Cassettes;
import atm.department.app.Nominal;
import atm.department.exceptions.ExceedMaxSumPerOperationException;
import atm.department.exceptions.OutOfMoneyException;
import atm.department.exceptions.UnsupportedAmountRequestedException;
import atm.department.state.AtmState;
import atm.department.state.CassettesState;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AtmImpl implements Atm {

    private int maxSumPerOperation;
    private Cassettes cassettes;
    private AtmLocationType atmType;


    public AtmImpl(AtmBuilder atmBuilder) {
        this.maxSumPerOperation = atmBuilder.getMaxSumPerOperation();
        this.cassettes = atmBuilder.getCassettes();
        this.atmType = atmBuilder.getAtmType();
    }

    public int getMaxSumPerOperation() {
        return maxSumPerOperation;
    }

    public Cassettes getCassettes() {
        return cassettes;
    }

    public AtmLocationType getAtmType() {
        return atmType;
    }

    public void withdrawMoney(int sumOfMoney) {
        log.debug("Total sum of money before withdraw:" + this.getTotalSumOfMoney());
        log.debug(String.format("Withdraw %d", sumOfMoney));
        checkSumOfMoneyCanBeGiven(sumOfMoney);
        this.cassettes.withdrawMoney(sumOfMoney);
        log.debug("Total sum of money after withdraw:" + this.getTotalSumOfMoney());
    }

    public int getTotalSumOfMoney() {
        return this.cassettes.getTotalSumOfMoney();
    }

    @Override
    public AtmState getState() {
        CassettesState cassettesState = this.cassettes.getState();

        return new AtmState() {
            @Override
            public CassettesState getCassettesState() {
                return cassettesState;
            }
        };
    }

    @Override
    public void restoreState(AtmState state) {
        this.cassettes.restoreState(state.getCassettesState());
    }

    private void checkSumOfMoneyCanBeGiven(int sumOfMoney) {
        checkRequestedAmountNotExceedsLimit(sumOfMoney);
        checkEnoughMoneyLeft(sumOfMoney);
        checkRequestedAmountMultipleExpected(sumOfMoney);
    }

    private void checkEnoughMoneyLeft(int requestedAmount) {
        if (this.getTotalSumOfMoney() - requestedAmount < 0) {
            throw new OutOfMoneyException();
        }
    }

    private void checkRequestedAmountMultipleExpected(int requestedAmount) {
        var multiplicity = Nominal.values()[0].getValue();
        if (requestedAmount <= 0 || requestedAmount % multiplicity != 0) {
            throw new UnsupportedAmountRequestedException(multiplicity);
        }
    }

    private void checkRequestedAmountNotExceedsLimit(int requestedAmount) {
        if (requestedAmount > this.getMaxSumPerOperation()) {
            throw new ExceedMaxSumPerOperationException(this.getMaxSumPerOperation());
        }
    }
}
