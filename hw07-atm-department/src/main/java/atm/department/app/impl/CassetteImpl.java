package atm.department.app.impl;


import atm.department.app.Cassette;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.state.CassetteState;

import java.util.Objects;

final public class CassetteImpl implements Cassette {

    private final CassetteConfiguration configuration;
    private final Nominal nominal;
    private int quantity;

    public CassetteImpl(CassetteConfiguration configuration) {
        this.configuration = configuration;
        this.quantity = this.configuration.getMaxNoteCapacity();
        this.nominal = this.configuration.getNominal();
    }

    @Override
    public void withdrawNotes(int quantity) {
        if (hasEnoughBanknotes(quantity)) {
            this.quantity -= quantity;
            return;
        }

        throw new OutOfBanknoteException(this.nominal);
    }

    @Override
    public int calculateActualBankNoteQuantityPerRequestedSumOfMoney(int sumOfMoney) {
        if (this.hasAtLeastOneNoteForRequestedSum(sumOfMoney)) {
            int requestedQuantity = getBankNoteQuantityForRequestedSum(sumOfMoney);

            return calculateActualBankNoteQuantityVsRequestedQuantity(requestedQuantity);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cassette cassette = (Cassette) o;

        return quantity == cassette.getQuantity() &&
                nominal == cassette.getNominal();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nominal, quantity);
    }

    @Override
    public String toString() {
        return String.format("Cassette{nominal=%s, quantity=%d}", this.nominal, this.quantity);
    }

    @Override
    public int compareTo(Cassette o) {
        return this.nominal.compareTo(o.getNominal());
    }

    @Override
    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int getAmount() {
        return quantity * nominal.getValue();
    }

    @Override
    public CassetteState getState() {
        var quantity = this.getQuantity();
        var nominal = this.getNominal();

        return new CassetteState() {
            @Override
            public int getQuantity() {
                return quantity;
            }

            @Override
            public Nominal getNominal() {
                return nominal;
            }

        };
    }

    @Override
    public void restoreState(CassetteState state) {
        this.setQuantity(state.getQuantity());
    }

    private boolean isEmpty() {
        return this.getQuantity() == 0;
    }

    private boolean hasAtLeastOneNoteForRequestedSum(int sumOfMoney) {
        return (getBankNoteQuantityForRequestedSum(sumOfMoney) > 0) && !this.isEmpty();
    }

    private int getBankNoteQuantityForRequestedSum(int sumOfMoney) {
        return sumOfMoney / this.nominal.getValue();
    }

    private int calculateActualBankNoteQuantityVsRequestedQuantity(int requestedQuantity) {
        return this.hasEnoughBanknotes(requestedQuantity)
                ? requestedQuantity : this.getQuantity();
    }

    private boolean hasEnoughBanknotes(int quantity) {
        return this.quantity - quantity >= 0;
    }
}
