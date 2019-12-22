package atm.department.model;

import atm.department.app.AtmLocationType;
import atm.department.app.Calculable;
import atm.department.event.EventListener;
import atm.department.event.Events;
import atm.department.model.state.AtmState;
import atm.department.model.state.CassetteState;
import atm.department.model.state.Memento;
import atm.department.model.state.State;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Atm implements Calculable, State, EventListener {

    private final int identifier;
    private final Memento initialMementoState;
    private int maxSumPerOperation;
    private CassettesCollection cassettesCollection;
    private AtmLocationType atmLocationType;

    public Atm(int identifier, int maxSumPerOperation, CassettesCollection cassettesCollection, AtmLocationType atmLocationType) {
        this.identifier = identifier;
        this.maxSumPerOperation = maxSumPerOperation;
        this.cassettesCollection = cassettesCollection;
        this.atmLocationType = atmLocationType;
        this.initialMementoState = this.saveState();
    }

    public int getAmountOfMoney() {
        return this.cassettesCollection.getAmountOfMoney();
    }

    public int getMultiplicityParameter() {
        return this.cassettesCollection.getLowestNominal().getValue();
    }

    public AtmState getState() {
        return new AtmState(this.maxSumPerOperation, this.cassettesCollection.getCassettesListState(), this.identifier);
    }

    @Override
    public Memento saveState() {
        return new Memento(getState());
    }

    @Override
    public void restoreState(Memento memento) {
        AtmState atmState = (AtmState) memento.getState();

        for (Cassette cassette : this.cassettesCollection) {
            for (CassetteState state : atmState.getCassetteStatesList()) {
                if (state.getNominal().equals(cassette.getNominal())) {
                    cassette.restoreState(new Memento(state));
                }
            }
        }

        this.setMaxSumPerOperation(atmState.getMaxSumPerOperation());
    }

    @Override
    public void update(Events eventType) {
        if (eventType.equals(Events.RESTORE)) {
            this.restoreState(initialMementoState);
        }
    }
}