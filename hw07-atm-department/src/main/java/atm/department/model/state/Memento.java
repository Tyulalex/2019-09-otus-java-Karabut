package atm.department.model.state;

public class Memento<T> {

    private final T state;

    public Memento(T state) {
        this.state = state;
    }

    public T getState() {
        return this.state;
    }
}
