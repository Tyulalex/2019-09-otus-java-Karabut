package atm.emulator.app;

import atm.emulator.configuration.CassetteConfiguration;
import atm.emulator.exceptions.OutOfBanknoteException;

import java.util.Objects;

public class Cassette implements Comparable<Cassette> {

    private final Nominal nominal;
    private final CassetteConfiguration configuration;
    private int quantity;

    public Cassette(Nominal nominal) {
        this(nominal, () -> 100);
    }

    public Cassette(Nominal nominal, CassetteConfiguration configuration) {
        this.nominal = nominal;
        this.configuration = configuration;
        this.quantity = this.configuration.getMaxNoteCapacity();
    }

    void withdraw(int quantity) {
        if (hasEnoughBanknotes(quantity)) {
            this.quantity -= quantity;
            return;
        }

        throw new OutOfBanknoteException(this.nominal);
    }

    int calculateActualBankNoteQuantityPerRequestedSumOfMoney(int sumOfMoney) {
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

        return quantity == cassette.quantity &&
                nominal == cassette.nominal;
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
        return this.nominal.compareTo(o.nominal);
    }

    Nominal getNominal() {
        return nominal;
    }

    int getQuantity() {
        return quantity;
    }

    Integer getAmount() {
        return quantity * nominal.getValue();
    }

    private boolean isEmpty() {
        return this.quantity == 0;
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
