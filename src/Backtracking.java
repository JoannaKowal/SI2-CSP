import java.util.*;

public class Backtracking {
    Futoshiki futoshiki;
    List<Cell> cellsOrder;
    int currentIndex;
    int numberOfCalls;

    public Backtracking(Futoshiki futoshiki)
    {
        this.futoshiki = futoshiki;
        cellsOrder = new ArrayList<>();
        setCellsOrder();
//        setCellsOrderMostLimited();
        this.currentIndex = 0;
        this.numberOfCalls = 0;
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
    private void setCellsOrderMostLimited()
    {
        cellsOrder.sort(new Comparator<Cell>() {
            @Override
            public int compare(Cell cell1, Cell cell2) {

                if(cell1.getLeftInDomain().size() > cell2.getLeftInDomain().size())
                {
                    return 1;
                } else if (cell1.getLeftInDomain().size() < cell2.getLeftInDomain().size()){
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
    private Cell findNextCell()
    {
        int minValue = Integer.MAX_VALUE;
        Cell selectedCell = null;
        for(int i = 0; i < futoshiki.getBoard().size(); i++)
        {
            for(int j = 0; j < futoshiki.getBoard().size(); j++)
            {
                if(!futoshiki.getBoard().get(i).get(j).isSet())
                {
                    Cell cell = futoshiki.getBoard().get(i).get(j);
                    if(cell.getLeftInDomain().size() < minValue)
                    {
                        minValue = cell.getLeftInDomain().size();
                        selectedCell = cell;
                    }
                }
            }
        }
        return  selectedCell;
    }
    public Map<Integer, Integer> valuesFrequences(Cell cell)
    {
        Map<Integer, Integer> frequencies = new HashMap<>();
        for(int i = 0; i < cell.getConstraints().size(); i++)
        {
           for(int j = 0; j < cell.getConstraints().get(i).getCells().size(); j++)
           {
               Cell other =  cell.getConstraints().get(i).getCells().get(j);
               if(!other.equals(cell) && !other.isSet())
               {
                   for(int k = 0; k < other.getLeftInDomain().size(); k++)
                   {
                       int value = other.getLeftInDomain().get(k);
                       if(frequencies.get(value) == null)
                       {
                           frequencies.put(value, 1);
                       }
                       else
                       {
                           frequencies.put(value, frequencies.get(value) + 1);
                       }
                   }
               }
           }
        }
        return frequencies;
    }

    public List<Integer> sortDomain(Cell cell) //sortowanie według częstości występowania wartości w ograniczeniach
    {
       Map<Integer, Integer> frequencies =  valuesFrequences(cell);
       List <Pair> sortedPairs = new ArrayList<>();
       List<Integer> domain = cell.getLeftInDomain();
       for(int i = 0; i < domain.size(); i++)
       {
           if(frequencies.get(domain.get(i)) == null)
           {
               Pair pair = new Pair(domain.get(i), 100);
               sortedPairs.add(pair);
           }
           else
           {
               Pair pair = new Pair(domain.get(i), frequencies.get(domain.get(i)));
               sortedPairs.add(pair);
           }

       }
       sortedPairs.sort(new Comparator<Pair>() {
           @Override
           public int compare(Pair pair1, Pair pair2) {

               if(pair1.getValue() > pair2.getValue())
               {
                   return 1;
               }
               else if(pair1.getValue() < pair2.getValue())
               {
                   return -1;
               }
               return 0;
           }
       });
       List<Integer> sortedDomain = new ArrayList<>();
       for(int i = 0; i < sortedPairs.size(); i++)
       {
           sortedDomain.add(sortedPairs.get(i).getKey());
       }
       return sortedDomain;
    }
    public boolean run()
    {
        numberOfCalls++;
        if(currentIndex == cellsOrder.size())
        {
            return  true;
        }
//        Cell currentCell = cellsOrder.get(currentIndex);
        Cell currentCell = findNextCell();
        List<Integer> domain = currentCell.getLeftInDomain();
//        List<Integer> domain = sortDomain(currentCell);
        for(int  i = 0; i < domain.size(); i++)
        {
            if(currentCell.getValue() > 0)
            {
                futoshiki.resetDomain(currentCell);
            }
            currentCell.setValue(domain.get(i));
            futoshiki.adjustDomains();
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
        if(currentCell.isSet())
        {
            futoshiki.resetDomain(currentCell);
        }
        currentCell.reset();
        currentIndex--;
        return  false;
    }

}
