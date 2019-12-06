package atm.department.app.impl;

import atm.department.app.CassetteService;
import atm.department.app.CassettesService;
import atm.department.app.Nominal;
import atm.department.exceptions.AtmOperationException;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.model.Cassette;
import atm.department.model.CassettesCollection;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;

@Slf4j
public class CassettesServiceImpl implements CassettesService {

    private final CassettesCollection cassettesCollection;

    public CassettesServiceImpl(CassettesCollection cassettesCollection) {
        this.cassettesCollection = cassettesCollection;
    }

    @Override
    public void withdrawMoney(int requestedSumOfMoney) throws AtmOperationException {
        performWithdrawForProvidedNominalAndQuantity(calculateBankNotesByNominal(requestedSumOfMoney));
    }

    private EnumMap<Nominal, Integer> calculateBankNotesByNominal(int requestedSumOfMoney) throws OutOfBanknoteException {
        int sumOfMoneyToGive = requestedSumOfMoney;
        EnumMap<Nominal, Integer> banknotesNominalToQuantityMap = new EnumMap<>(Nominal.class);
        for (Cassette cassette : this.cassettesCollection) {
            Nominal nominal = cassette.getNominal();
            CassetteService cassetteService = new CassetteServiceImpl(cassette);
            int banknoteQuantityToWithdraw =
                    cassetteService.calculateActualBankNoteQuantityPerRequestedSumOfMoney(sumOfMoneyToGive);
            banknotesNominalToQuantityMap.put(nominal, banknoteQuantityToWithdraw);
            sumOfMoneyToGive -= banknoteQuantityToWithdraw * nominal.getValue();
            if (sumOfMoneyToGive == 0) {
                break;
            }
        }

        checkMoneyLeftToGiveZero(sumOfMoneyToGive);

        return banknotesNominalToQuantityMap;
    }

    private void performWithdrawForProvidedNominalAndQuantity(EnumMap<Nominal, Integer> banknotesNominalToQuantity) throws AtmOperationException {
        log.info("Withdraw banknotes of nominals as follow {}", banknotesNominalToQuantity);
        for (var entry : banknotesNominalToQuantity.entrySet()) {
            Cassette cassette = this.cassettesCollection.getCassetteByNominal(entry.getKey());
            if (cassette != null) {
                new CassetteServiceImpl(cassette).withdrawNotes(entry.getValue());
            } else {
                log.error("unable to perform withdraw for nominal {}", entry.getKey());
                throw new AtmOperationException("Unable to perform operation");
            }
        }
    }

    private void checkMoneyLeftToGiveZero(int sumOfMoney) throws OutOfBanknoteException {
        if (sumOfMoney != 0) {
            throw new OutOfBanknoteException();
        }
    }
}
