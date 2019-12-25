package atm.department.model;

import atm.department.app.Calculable;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.model.state.CassetteState;
import atm.department.model.state.Memento;
import atm.department.model.state.State;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Cassette implements Comparable<Cassette>, State, Calculable {

    private final int maxNoteCapacity;
    private final Nominal nominal;
    private int quantity = 0;

    public Cassette(CassetteConfiguration configuration) {
        this(configuration, configuration.getMaxNoteCapacity());
    }

    public Cassette(CassetteConfiguration configuration, int quantity) {
        this.nominal = configuration.getNominal();
        this.maxNoteCapacity = configuration.getMaxNoteCapacity();
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Cassette{maxNoteCapacity=%d, nominal=%s, quantity=%d}",
                this.maxNoteCapacity, this.nominal, this.quantity);
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
    public int compareTo(Cassette o) {
        return this.nominal.compareTo(o.getNominal());
    }


    public CassetteState getState() {
        return new CassetteState(this.quantity, this.nominal);
    }

    @Override
    public Memento saveState() {
        return new Memento(this.getState());
    }

    @Override
    public void restoreState(Memento memento) {
        CassetteState cassetteState = (CassetteState) memento.getState();
        this.setQuantity(cassetteState.getQuantity());
    }

    public boolean isEmpty() {
        return this.getQuantity() == 0;
    }

    @Override
    public int getAmountOfMoney() {
        return quantity * nominal.getValue();
    }
}
