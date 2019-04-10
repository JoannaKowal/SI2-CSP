import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cell {
    private int value;
    private List<Integer> leftInDomain;
    private List<Integer> wholeDomain;
    private List<Constraint> constraints;
    private List<Constraint> smallerThanConstraints;
    private List<Constraint> greaterThanConstraints;

    public List<Constraint> getSmallerThanConstraints() {
        return smallerThanConstraints;
    }

    public List<Integer> getLeftInDomain() {
        return leftInDomain;
    }

    public Cell(int value) {
        this.value = value;
        this.leftInDomain = new LinkedList<>();
        this.wholeDomain = new LinkedList<>();
        this.constraints = new ArrayList<>();
        this.smallerThanConstraints = new ArrayList<>();


    }
    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    public void addConstraint(Constraint constraint)
    {
        this.constraints.add(constraint);
    }
    public void addSmallerThan(Constraint constraint)
    {
        this.smallerThanConstraints.add(constraint);
    }
    public void addGraterThan(Constraint constraint)
    {
        this.greaterThanConstraints.add(constraint);
    }

    public void reset()
    {
        this.value = 0;

    }

    public List<Integer> getWholeDomain() {
        return wholeDomain;
    }

    public boolean isSet()
    {
        boolean isSet = true;
        if(this.value == 0)
        {
            isSet = false;
        }
        return isSet;
    }

}
