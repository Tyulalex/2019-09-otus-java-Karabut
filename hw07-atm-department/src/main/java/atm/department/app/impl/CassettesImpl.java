package atm.department.app.impl;


import atm.department.app.Cassette;
import atm.department.app.Cassettes;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.exceptions.AtmOperationException;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.state.CassetteState;
import atm.department.state.CassettesState;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

@Slf4j
final public class CassettesImpl implements Cassettes {

    private final List<Cassette> cassettes;

    public CassettesImpl(List<CassetteConfiguration> cassetteConfigurations) {
        this.cassettes = initCassettes(cassetteConfigurations);
    }

    public int getTotalSumOfMoney() {
        return this.cassettes.stream().mapToInt(Cassette::getAmount).sum();
    }

    @Override
    public void withdrawMoney(int requestedSumOfMoney) {
        performWithdrawForProvidedNominalAndQuantity(calculateBankNotesByNominal(requestedSumOfMoney));
    }

    @Override
    public Cassette getCassetteByNominal(Nominal nominal) {
        return this.cassettes.stream()
                .filter(cassette -> cassette.getNominal() == nominal)
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return this.cassettes.toString();
    }

    @Override
    public CassettesState getState() {
        List<CassetteState> cassetteStates = new ArrayList<>();
        this.cassettes.stream().forEach(cassette -> cassetteStates.add(cassette.getState()));

        return new CassettesState() {
            @Override
            public List<CassetteState> getCassettesState() {
                return cassetteStates;
            }
        };
    }

    @Override
    public void restoreState(CassettesState states) {
        for (Cassette cassette : this.cassettes) {
            for (CassetteState state : states.getCassettesState()) {
                if (state.getNominal().equals(cassette.getNominal())) {
                    cassette.restoreState(state);
                }
            }
        }
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

    private List<Cassette> initCassettes(List<CassetteConfiguration> cassetteConfiguration) {
        List<Cassette> cassettes = new ArrayList<>();
        for (CassetteConfiguration configuration : cassetteConfiguration) {
            cassettes.add(new CassetteImpl(configuration));
        }
        Collections.sort(cassettes, Collections.reverseOrder());
        return cassettes;
    }

    private void performWithdrawForProvidedNominalAndQuantity(EnumMap<Nominal, Integer> banknotesNominalToQuantity) {
        log.info("Withdraw banknotes of nominals as follow {}", banknotesNominalToQuantity);
        for (var entry : banknotesNominalToQuantity.entrySet()) {
            Cassette cassette = getCassetteByNominal(entry.getKey());
            if (cassette != null) {
                cassette.withdrawNotes(entry.getValue());
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
