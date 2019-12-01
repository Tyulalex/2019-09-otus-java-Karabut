package atm.emulator.app.impl;

import atm.emulator.app.Cassette;
import atm.emulator.app.Nominal;
import atm.emulator.configuration.CassetteConfiguration;
import atm.emulator.exceptions.OutOfBanknoteException;

import java.util.Objects;

final public class CassetteImpl implements Cassette, Comparable<Cassette> {

    private final Nominal nominal;
    private final CassetteConfiguration configuration;
    private int quantity;

    public CassetteImpl(Nominal nominal, CassetteConfiguration configuration) {
        this.nominal = nominal;
        this.configuration = configuration;
        this.quantity = this.configuration.getMaxNoteCapacity();
    }

    public void withdraw(int quantity) {
        if (hasEnoughBanknotes(quantity)) {
            this.quantity -= quantity;
            return;
        }

        throw new OutOfBanknoteException(this.nominal);
    }

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

    public Nominal getNominal() {
        return nominal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAmount() {
        return quantity * nominal.getValue();
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
