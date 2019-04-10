import java.util.ArrayList;
import java.util.List;

public class DistinctionConstraint implements Constraint {
    private List<Cell> constraints;

    public DistinctionConstraint() {
        this.constraints = new ArrayList<>();
    }

    @Override
    public List<Cell> getCells() {
        return constraints;
    }

    public boolean isSatisfied() {
        boolean isSatisfied = true;

        for(int i = 0; i < constraints.size() - 1; i++)
        {
            if(constraints.get(i).getValue() != 0) {
                for (int j = i + 1; j < constraints.size(); j++) {
                    if (constraints.get(j).getValue() != 0) {
                        isSatisfied = !(constraints.get(j).getValue() == constraints.get(i).getValue());
                        if(!isSatisfied)
                        {
                            return  isSatisfied;
                        }
                    }
                }
            }

        }
        return isSatisfied;
    }
    }
