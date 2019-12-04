package atm.department.app.impl;

import atm.department.app.Atm;
import atm.department.app.AtmDepartment;
import atm.department.app.Calculable;
import atm.department.state.AtmDepartmentState;
import atm.department.state.AtmState;

import java.util.ArrayList;
import java.util.List;

final public class AtmDepartmentImpl implements AtmDepartment {

    private List<Atm> atms;
    private List<AtmState> atmStates = new ArrayList<>();
    private final AtmDepartmentState atmDepartmentInitialState;

    public AtmDepartmentImpl(List<Atm> atms) {
        this.atms = List.copyOf(atms);
        this.atmDepartmentInitialState = getState();
    }

    public List<Atm> getAtms() {
        return atms;
    }

    @Override
    public int getTotalSumOfMoney() {
        return this.atms.stream().mapToInt(Calculable::getTotalSumOfMoney).sum();
    }

    @Override
    public AtmDepartmentState getState() {
        this.atmStates = new ArrayList<>();
        atms.forEach(atm -> atmStates.add(atm.getState()));

        return new AtmDepartmentState() {
            @Override
            public List<AtmState> getAtmsState() {
                return atmStates;
            }
        };

    }

    @Override
    public void restoreState(AtmDepartmentState state) {
        for (Atm atm : this.atms) {
            for (AtmState atmState : state.getAtmsState()) {
                atm.restoreState(atmState);
            }
        }
    }

    public void restoreToInitState() {
        this.restoreState(this.atmDepartmentInitialState);
    }
}
