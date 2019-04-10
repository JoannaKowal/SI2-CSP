import java.util.ArrayList;
import java.util.List;

public class Backtracking {
    Futoshiki futoshiki;
    List<Cell> cellsOrder;
    int currentIndex;

    public Backtracking(Futoshiki futoshiki)
    {
        this.futoshiki = futoshiki;
        cellsOrder = new ArrayList<>();
        setCellsOrder();
        this.currentIndex = 0;
    }

    private void setCellsOrder()
    {
        for(int i = 0; i < futoshiki.getBoard().size(); i++)
        {
            for(int j = 0; j < futoshiki.getBoard().size(); j++)
            {
                if(!futoshiki.getBoard().get(i).get(j).isSet())
                {
                    cellsOrder.add(futoshiki.getBoard().get(i).get(j));
                }
            }
        }
    }
    public boolean run()
    {
        if(currentIndex == cellsOrder.size())
        {
            return  true;
        }
        Cell currentCell = cellsOrder.get(currentIndex);
        List<Integer> domain = currentCell.getLeftInDomain();
        for(int  i = 0; i < domain.size(); i++)
        {
            currentCell.setValue(currentCell.getLeftInDomain().get(i));
            boolean valueCorrect = true;
            for (Constraint constraint : currentCell.getConstraints())
            {
                if(!constraint.isSatisfied())
                {
                    valueCorrect = false;
                    break;
                }
            }
            if(valueCorrect) {
                for (Constraint smallerThanConstraint : currentCell.getSmallerThanConstraints()) {
                    if (!smallerThanConstraint.isSatisfied()) {
                        valueCorrect = false;
                        break;
                    }
                }
            }
            if(valueCorrect)
            {
                currentIndex++;
                if(run())
                {
                    return true;
                }
                continue;
            }
        }
        currentCell.reset();
        currentIndex--;
        return  false;
    }
}
