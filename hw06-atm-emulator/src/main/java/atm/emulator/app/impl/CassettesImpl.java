package atm.emulator.app.impl;

import atm.emulator.app.Cassette;
import atm.emulator.app.Cassettes;
import atm.emulator.app.Nominal;
import atm.emulator.configuration.CassetteConfiguration;
import atm.emulator.exceptions.AtmOperationException;
import atm.emulator.exceptions.OutOfBanknoteException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@Slf4j
final public class CassettesImpl implements Cassettes {

    private final List<Cassette> cassettes;

    public CassettesImpl(CassetteConfiguration cassetteConfiguration) {
        this.cassettes = initCassettes(cassetteConfiguration);
    }

    public CassettesImpl(List<Cassette> cassettes) {
        this.cassettes = List.copyOf(cassettes);
    }

    public int getTotalSumOfMoney() {
        return this.cassettes.stream().mapToInt(Cassette::getAmount).sum();
    }

    public void performWithdraw(int requestedSumOfMoney) {
        performWithdrawForProvidedNominalAndQuantity(calculateBankNotesByNominal(requestedSumOfMoney));
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

    private EnumMap<Nominal, Integer> calculateBankNotesByNominal(int requestedSumOfMoney) {
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

    private List<Cassette> initCassettes(CassetteConfiguration cassetteConfiguration) {
        List<Cassette> cassettes = new ArrayList<>();
        for (int i = Nominal.values().length - 1; i >= 0; i--) {
            cassettes.add(new CassetteImpl(Nominal.values()[i], cassetteConfiguration));
        }
        return cassettes;
    }

    private void performWithdrawForProvidedNominalAndQuantity(EnumMap<Nominal, Integer> banknotesNominalToQuantity) {
        log.info("Withdraw banknotes of nominals as follow {}", banknotesNominalToQuantity);
        for (var entry : banknotesNominalToQuantity.entrySet()) {
            Cassette cassette = getCassetteByNominal(entry.getKey());
            if (cassette != null) {
                cassette.withdraw(entry.getValue());
            } else {
                log.error("unable to perform withdraw for nominal {}", entry.getKey());
                throw new AtmOperationException("Unable to perform operation");
            }
        }
    }

    private void checkMoneyLeftToGiveZero(int sumOfMoney) {
        if (sumOfMoney != 0) {
            throw new OutOfBanknoteException();
        }
    }
}
