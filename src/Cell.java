import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cell {
    private int value;
    private int index;
    private int numberOfDependents;
    private List<Integer> leftInDomain;
    private List<Integer> wholeDomain;
    private List<Constraint> constraints;
    private List<Constraint> smallerThanConstraints;
    private List<Constraint> allConstraints;
    private List<SkyscrapperConstraint> skyConstraints;
    final int constraintsSize = 2; // constrainty dla komórki: wiersz i kolumna


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Constraint> getSmallerThanConstraints() {
        return smallerThanConstraints;
    }

    public List<Integer> getLeftInDomain() {
        return leftInDomain;
    }

    public List<SkyscrapperConstraint> getSkyConstraints() {
        return skyConstraints;
    }

    public Cell(int value) {
        this.value = value;
        this.leftInDomain = new LinkedList<>();
        this.wholeDomain = new LinkedList<>();
        this.constraints = new ArrayList<>();
        this.smallerThanConstraints = new ArrayList<>();
        this.allConstraints = new ArrayList<>();
        this.numberOfDependents = 0;
        this.skyConstraints = new ArrayList<>();

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
        this.allConstraints.add(constraint);
    }
    public void addSmallerThan(Constraint constraint)
    {
        this.smallerThanConstraints.add(constraint);
        this.allConstraints.add(constraint);
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
    public boolean recalculateDomain()
    {
        boolean result = true;
        leftInDomain.clear();
        leftInDomain.addAll(getWholeDomain());

        for (int i = 0; i < leftInDomain.size();)
        {
            this.value = leftInDomain.get(i);

            boolean valueOk = true;
            for (int ci = 0; ci < allConstraints.size(); ci++)
            {
                if (!allConstraints.get(ci).isSatisfied())
                {
                    valueOk = false;
                    break;
                }
            }
            this.value = 0;

            if (valueOk)
            {
                i++;
            }
            else
            {
                leftInDomain.remove(i);
                if(leftInDomain.size() == 0)
                {
                    result = false;
                }
            }
        }
        return  result;
    }

    public boolean updateConstrainedDomains()
    {
        boolean result = true;
        this.numberOfDependents=0;
        for (int i = 0; i<constraintsSize; i++)
        {
            for (int j = 0; j < allConstraints.get(i).getCells().size(); j++)
            {
                Cell cell = allConstraints.get(i).getCells().get(j);

                if (!cell.isSet())
                {
                    if(!cell.recalculateDomain())
                    {
                        result = false;
                    }
                    this.numberOfDependents++;
                }
            }
        }
        return  result;
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
    public  boolean checkSkyConstraints()
    {
        if(checkFromTop() && checkFromBottom() && checkFromLeft() && checkFromRight())
        {
            return true;
        }
        return  false;

    }
       private boolean checkFromTop()
    {
        int visibleSkyscrappers = 0;
        boolean isSatisfied = true;
        int height = 0;
        if(skyConstraints.get(0).getConstraints().get(0) == 0)
        {
            return  true;
        }
        for(int i = 0; i < allConstraints.get(1).getCells().size(); i++) // allConstraints.get(1) to kolumna
        {
            Cell cell = allConstraints.get(1).getCells().get(i);
            if(cell.isSet())
            {
                if(cell.getValue() > height)
                {
                    height = cell.getValue();
                    visibleSkyscrappers++;
                }
            }
            else
            {
                if(visibleSkyscrappers > skyConstraints.get(0).getConstraints().get(0))
                {
                    isSatisfied = false;
                    return  isSatisfied;
                }
                else
                {
                    return isSatisfied;
                }
            }
        }
        if(visibleSkyscrappers != skyConstraints.get(0).getConstraints().get(0))
        {
            isSatisfied = false;
        }
        return isSatisfied;
    }

    private boolean checkFromBottom()
    {
        int visibleSkyscrappers = 0;
        boolean isSatisfied = true;
        int height = 0;
        if(skyConstraints.get(0).getConstraints().get(1) == 0)
        {
            return  true;
        }
        for(int i = allConstraints.get(1).getCells().size() - 1; i >= 0 ; i--) // allConstraints.get(1) to kolumna
        {
            Cell cell = allConstraints.get(1).getCells().get(i);
            if(cell.isSet())
            {
                if(cell.getValue() > height)
                {
                    height = cell.getValue();
                    visibleSkyscrappers++;
                }
            }
            else
            {
                if(visibleSkyscrappers > skyConstraints.get(0).getConstraints().get(1))
                {
                    isSatisfied = false;
                    return  isSatisfied;
                }
                else
                {
                    return isSatisfied;
                }
            }
        }
        if(visibleSkyscrappers != skyConstraints.get(0).getConstraints().get(1))
        {
            isSatisfied = false;
        }
        return isSatisfied;
    }
    private boolean checkFromLeft()
    {
        int visibleSkyscrappers = 0;
        boolean isSatisfied = true;
        int height = 0;
        if(skyConstraints.get(0).getConstraints().get(2) == 0)
        {
            return  true;
        }
        for(int i = 0; i < allConstraints.get(0).getCells().size() ; i++) // allConstraints.get(0) to wiersz
        {
            Cell cell = allConstraints.get(0).getCells().get(i);
            if(cell.isSet())
            {
                if(cell.getValue() > height)
                {
                    height = cell.getValue();
                    visibleSkyscrappers++;
                }
            }
            else
            {
                if(visibleSkyscrappers > skyConstraints.get(0).getConstraints().get(2)) //lewy
                {
                    isSatisfied = false;
                    return  isSatisfied;
                }
                else
                {
                    return isSatisfied;
                }
            }
        }
        if(visibleSkyscrappers != skyConstraints.get(0).getConstraints().get(2))
        {
            isSatisfied = false;
        }
        return isSatisfied;
    }
    private boolean checkFromRight()
    {
        int visibleSkyscrappers = 0;
        boolean isSatisfied = true;
        int height = 0;
        if(skyConstraints.get(0).getConstraints().get(3) == 0)
        {
            return  true;
        }
        for(int i = allConstraints.get(0).getCells().size() - 1; i >= 0 ; i--) // allConstraints.get(0) to wiersz
        {
            Cell cell = allConstraints.get(0).getCells().get(i);
            if(cell.isSet())
            {
                if(cell.getValue() > height)
                {
                    height = cell.getValue();
                    visibleSkyscrappers++;
                }
            }
            else
            {
                if(visibleSkyscrappers > skyConstraints.get(0).getConstraints().get(3)) //prawy
                {
                    isSatisfied = false;
                    return  isSatisfied;
                }
                else
                {
                    return isSatisfied;
                }
            }
        }
        if(visibleSkyscrappers != skyConstraints.get(0).getConstraints().get(3))
        {
            isSatisfied = false;
        }
        return isSatisfied;
    }



    public int getNumberOfDependents() {
        return numberOfDependents;
    }
}
