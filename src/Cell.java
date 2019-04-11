import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cell {
    private int value;
    private List<Integer> leftInDomain;
    private List<Integer> wholeDomain;
    private List<Constraint> constraints;
    private List<Constraint> smallerThanConstraints;

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
    public void updateDomain() //zawężanie dziedziny
    {
        for (int i = 0; i < constraints.size(); i++) {
            for (int j = 0; j < constraints.get(i).getCells().size(); j++) {
                Cell cell = constraints.get(i).getCells().get(j);
                int value = cell.getValue();
                if (value > 0) {
                    int index = leftInDomain.indexOf(value);
                    if (index >= 0) {
                        leftInDomain.remove(index);
                    }
                }
            }
        }

        int size = wholeDomain.size();
        for(int i = 0; i < smallerThanConstraints.size(); i++)
        {
            for(int j = 0; j < smallerThanConstraints.get(i).getCells().size(); j++)
            {
                Cell cell = smallerThanConstraints.get(i).getCells().get(0);
                if(cell.equals(this))
                {
                    int index = leftInDomain.indexOf(size);
                    if(index >= 0)
                    {
                        leftInDomain.remove(index);
                    }
                }
                else
                {
                    int index = leftInDomain.indexOf(1);
                    if(index >= 0)
                    {
                        leftInDomain.remove(index);
                    }
                }
            }
        }
    }

}
