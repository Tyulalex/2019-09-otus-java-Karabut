package atm.department.app.impl;

import atm.department.app.AtmDepartmentService;
import atm.department.app.Calculable;
import atm.department.event.EventManager;
import atm.department.event.Events;
import atm.department.model.Atm;

import java.util.List;

final public class AtmDepartmentServiceImpl implements AtmDepartmentService {

    public EventManager events;

    private List<Atm> atms;

    public AtmDepartmentServiceImpl(List<Atm> atms) {
        this.events = new EventManager(Events.RESTORE);
        this.atms = atms;
        for (Atm atm : this.atms) {
            this.events.subscribe(Events.RESTORE, atm);
        }
    }

    public void restoreToInitStateEvent() {
        events.notify(Events.RESTORE);
    }

    @Override
    public int getCurrentBalance() {
        return this.atms.stream().mapToInt(Calculable::getAmountOfMoney).sum();
    }

    @Override
    public void resetAtmsToInitialState() {
        events.notify(Events.RESTORE);
    }
}
