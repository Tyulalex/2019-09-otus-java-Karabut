package atm.department.app.impl;


import atm.department.app.CassetteService;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.model.Cassette;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
final public class CassetteServiceImpl implements CassetteService {

    private Cassette cassette;

    public CassetteServiceImpl(Cassette cassette) {
        this.cassette = cassette;
    }

    @Override
    public void withdrawNotes(int quantity) throws OutOfBanknoteException {
        if (hasEnoughBanknotes(quantity)) {
            this.cassette.setQuantity(this.cassette.getQuantity() - quantity);
            return;
        }

        throw new OutOfBanknoteException(this.cassette.getNominal());
    }

    @Override
    public int calculateActualBankNoteQuantityPerRequestedSumOfMoney(int sumOfMoney) {
        if (this.hasAtLeastOneNoteForRequestedSum(sumOfMoney)) {
            int requestedQuantity = getBankNoteQuantityForRequestedSum(sumOfMoney);

            return calculateActualBankNoteQuantityVsRequestedQuantity(requestedQuantity);
        }
        return 0;
    }

    private boolean hasAtLeastOneNoteForRequestedSum(int sumOfMoney) {
        return getBankNoteQuantityForRequestedSum(sumOfMoney) > 0 && !this.cassette.isEmpty();
    }

    private int getBankNoteQuantityForRequestedSum(int sumOfMoney) {
        return sumOfMoney / this.cassette.getNominal().getValue();
    }

    private int calculateActualBankNoteQuantityVsRequestedQuantity(int requestedQuantity) {
        return this.hasEnoughBanknotes(requestedQuantity)
                ? requestedQuantity : this.cassette.getQuantity();
    }

    private boolean hasEnoughBanknotes(int quantity) {
        return this.cassette.getQuantity() - quantity >= 0;
    }
}
