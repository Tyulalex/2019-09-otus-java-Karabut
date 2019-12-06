package atm.department.model.state;

public interface State {

    Memento saveState();

    void restoreState(Memento memento);

}
