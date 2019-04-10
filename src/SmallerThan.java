import java.util.ArrayList;
import java.util.List;

public class SmallerThan implements Constraint {
    private List<Cell> constraints;

    public SmallerThan() {
        constraints = new ArrayList<>();
    }

    @Override
    public List<Cell> getCells() {
        return constraints;
    }

    @Override
    public boolean isSatisfied()
    {
        boolean isSatisfied = true;
        if(constraints.get(0).getValue()!= 0 && constraints.get(1).getValue() != 0) {
            isSatisfied = constraints.get(0).getValue() < constraints.get(1).getValue();
        }
        return isSatisfied;
    }
}
