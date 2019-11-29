package atm.emulator;

import atm.emulator.app.Atm;
import atm.emulator.app.Cassettes;
import atm.emulator.exceptions.AtmOperationException;
import lombok.extern.log4j.Log4j;

@Log4j
public class Main {

    public static void main(String[] args) {
        Atm atm = new Atm(() -> 50000, new Cassettes(() -> 100));
        try {
            atm.withdrawMoney(5000);
            atm.withdrawMoney(5400);
        } catch (AtmOperationException ex) {
            log.warn(ex.getMessage());
        }
        log.info(atm.getCassettes());
    }
}
