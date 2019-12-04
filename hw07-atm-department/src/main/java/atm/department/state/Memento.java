package atm.department.state;

public interface Memento<T extends State> {

    T getState();

    void restoreState(T state);
}
