package atm.emulator.app;

import atm.emulator.configuration.CassetteConfiguration;
import atm.emulator.exceptions.AtmOperationException;
import atm.emulator.exceptions.OutOfBanknoteException;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@Log4j
public class Cassettes {

    private List<Cassette> cassettes = new ArrayList<>();

    public Cassettes(CassetteConfiguration cassetteConfiguration) {
        for (int i = Nominal.values().length - 1; i >= 0; i--) {
            this.cassettes.add(new Cassette(Nominal.values()[i], cassetteConfiguration));
        }
    }

    public EnumMap<Nominal, Integer> calculateBankNotesByNominal(int requestedSumOfMoney) {
        int sumOfMoneyToGive = requestedSumOfMoney;
        EnumMap<Nominal, Integer> banknotesNominalToQuantityMap = new EnumMap<>(Nominal.class);
        for (Cassette cassette : cassettes) {

            Nominal nominal = cassette.getNominal();
            int banknoteQuantityToWithdraw =
                    cassette.calculateActualBankNoteQuantityPerRequestedSumOfMoney(sumOfMoneyToGive);
            banknotesNominalToQuantityMap.put(nominal, banknoteQuantityToWithdraw);
            sumOfMoneyToGive -= banknoteQuantityToWithdraw * nominal.getValue();
            if (sumOfMoneyToGive == 0) {
                break;
            }
        }

        checkMoneyLeftToGiveZero(sumOfMoneyToGive);

        return banknotesNominalToQuantityMap;
    }

    public int getTotalSumOfMoney() {
        return this.cassettes.stream().mapToInt(Cassette::getAmount).sum();
    }

    public void performWithdraw(EnumMap<Nominal, Integer> banknotesNominalToQuantity) {
        log.info(banknotesNominalToQuantity);
        for (var entry : banknotesNominalToQuantity.entrySet()) {
            Cassette cassette = getCassetteByNominal(entry.getKey());
            if (cassette != null) {
                cassette.withdraw(entry.getValue());
            } else {
                log.error(String.format("unable to perform withdraw for nominal %s", entry.getKey()));
                throw new AtmOperationException("Unable to perform operation");
            }
        }
    }

    public Cassette getCassetteByNominal(Nominal nominal) {
        return this.cassettes.stream()
                .filter(cassette -> cassette.getNominal() == nominal)
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return this.cassettes.toString();
    }

    private void checkMoneyLeftToGiveZero(int sumOfMoney) {
        if (sumOfMoney != 0) {
            throw new OutOfBanknoteException();
        }
    }
}
