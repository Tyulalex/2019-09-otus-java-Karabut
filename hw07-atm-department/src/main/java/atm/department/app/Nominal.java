package atm.department.app;

public enum Nominal {

    N50(50),
    N100(100),
    N200(200),
    N500(500),
    N1000(1000),
    N2000(2000),
    N5000(5000);

    private final int value;

    Nominal(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Nominal{value=%d}", this.value);
    }
}
