package atm.emulator.app.impl;

import atm.emulator.app.Atm;
import atm.emulator.app.Cassettes;
import atm.emulator.app.Nominal;
import atm.emulator.configuration.AtmConfiguration;
import atm.emulator.exceptions.ExceedMaxSumPerOperationException;
import atm.emulator.exceptions.OutOfMoneyException;
import atm.emulator.exceptions.UnsupportedAmountRequestedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final public class AtmEmulatorImpl implements Atm {

    private final AtmConfiguration configuration;
    private final Cassettes cassettes;

    public AtmEmulatorImpl(AtmConfiguration configuration, Cassettes cassettes) {
        this.configuration = configuration;
        this.cassettes = cassettes;
    }

    public void withdrawMoney(int sumOfMoney) {
        log.debug("Total sum of money before withdraw:" + this.getTotalSumOfMoney());
        log.debug(String.format("Withdraw %d", sumOfMoney));
        checkSumOfMoneyCanBeGiven(sumOfMoney);
        this.cassettes.performWithdraw(sumOfMoney);
        log.debug("Total sum of money after withdraw:" + this.getTotalSumOfMoney());
    }

    public int getTotalSumOfMoney() {
        return this.cassettes.getTotalSumOfMoney();
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
        if (requestedAmount > this.configuration.getMaxSumPerOperation()) {
            throw new ExceedMaxSumPerOperationException(this.configuration.getMaxSumPerOperation());
        }
    }
}
