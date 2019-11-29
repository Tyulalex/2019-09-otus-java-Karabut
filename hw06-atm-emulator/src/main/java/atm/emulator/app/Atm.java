package atm.emulator.app;

import atm.emulator.configuration.AtmConfiguration;
import atm.emulator.exceptions.ExceedMaxSumPerOperationException;
import atm.emulator.exceptions.OutOfMoneyException;
import atm.emulator.exceptions.UnsupportedAmountRequestedException;
import lombok.extern.log4j.Log4j;

import java.util.EnumMap;

@Log4j
public class Atm {

    private final AtmConfiguration configuration;
    private Cassettes cassettes;

    public Atm(AtmConfiguration configuration, Cassettes cassettes) {
        this.configuration = configuration;
        this.cassettes = cassettes;
    }

    public Cassettes getCassettes() {
        return cassettes;
    }

    public void withdrawMoney(int sumOfMoney) {
        log.debug("Total sum of money before withdraw:" + this.getTotalSumOfMoney());
        log.debug(String.format("Withdraw %d", sumOfMoney));
        checkSumOfMoneyCanBeGiven(sumOfMoney);
        EnumMap<Nominal, Integer> banknotesNominalToQuantity = this.cassettes.calculateBankNotesByNominal(sumOfMoney);
        this.cassettes.performWithdraw(banknotesNominalToQuantity);
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
