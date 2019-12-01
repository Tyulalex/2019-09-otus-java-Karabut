package atm.emulator;

import atm.emulator.app.Atm;
import atm.emulator.app.impl.AtmEmulatorImpl;
import atm.emulator.app.impl.CassettesImpl;
import atm.emulator.exceptions.AtmOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
final public class Main {

    public static void main(String[] args) {
        CassettesImpl cassettes = new CassettesImpl(() -> 100);
        Atm atm = new AtmEmulatorImpl(() -> 50000, cassettes);
        try {
            atm.withdrawMoney(5000);
            atm.withdrawMoney(5400);
            atm.getTotalSumOfMoney();
        } catch (AtmOperationException ex) {
            log.warn(ex.getMessage());
        }
        log.info(String.format("Cassettes: %s", cassettes));
    }
}
