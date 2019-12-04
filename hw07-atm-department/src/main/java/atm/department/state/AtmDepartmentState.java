package atm.department.state;

import java.util.List;

public interface AtmDepartmentState extends State {

    List<AtmState> getAtmsState();
}
