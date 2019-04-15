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
    public int DFS(int pos)
    {
        int result = 0;
        for(int i = 0; i < smallerThanConstraints.size(); i++)
        {

                Cell cell = smallerThanConstraints.get(i).getCells().get(pos);
                if(cell.equals(this))
                {
                    Cell nextCell = smallerThanConstraints.get(i).getCells().get(1-pos);
                    result =  Math.max(result,nextCell.DFS(pos)+1);
                }


        }
        return result;

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
        int chainSize = DFS(0);
        for(int ci=size;ci>size-chainSize; ci--)
        {
            int index = leftInDomain.indexOf(ci);
            if(index >= 0)
            {
                leftInDomain.remove(index);
            }
        }
        chainSize = DFS(1);
        for(int ci=1;ci<=chainSize; ci++) {
            int index = leftInDomain.indexOf(ci);
            if (index >= 0) {
                leftInDomain.remove(index);
            }
        }

    }

    public boolean checkSkyConstraintsAll()
    {
        boolean isSatisfied = true;
        int start = 0;
        int end = 0;
        int delta = 0;
        int rowColumn = 0;//0 - wiersz, 1 - kolumna
        for(int dir = 0;dir <4;dir ++) {
            if(dir==0) {
                start = 0;
                end = allConstraints.get(1).getCells().size();
                delta = 1;
                rowColumn=1;
            }
            else if (dir==1) {
                start = allConstraints.get(1).getCells().size()-1;
                end = -1;
                delta = -1;
                rowColumn=1;
            }
            else if(dir==2) {
                start = 0;
                end = allConstraints.get(1).getCells().size();
                delta = 1;
                rowColumn=0;
            }
            else {
                start = allConstraints.get(1).getCells().size()-1;
                end = -1;
                delta = -1;
                rowColumn = 0;
            }

            int visibleSkyscrappers = 0;

            int height = 0;
            if (skyConstraints.get(0).getConstraints().get(dir) == 0) {
                continue;
            }
            boolean allSet = true;//caly rzad lub kolumna wypelnione
            for (int i = start; i != end; i+=delta) // allConstraints.get(1) to kolumna
            {
                Cell cell = allConstraints.get(rowColumn).getCells().get(i);
                if (cell.isSet()) {
                    if (cell.getValue() > height) {
                        height = cell.getValue();
                        visibleSkyscrappers++;
                    }
                } else {
                    if (visibleSkyscrappers > skyConstraints.get(0).getConstraints().get(dir)) {
                        isSatisfied = false;
                        return isSatisfied;
                    }
                    else {
                        allSet = false;
                        break;
                    }
                }
            }
            if(allSet)
            {
                if (visibleSkyscrappers != skyConstraints.get(0).getConstraints().get(dir)) {
                    isSatisfied = false;
                    return isSatisfied;
                }
            }
        }
        return isSatisfied;
    }



    public int getNumberOfDependents() {
        return numberOfDependents;
    }
}
