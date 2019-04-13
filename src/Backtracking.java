import java.util.*;

public class Backtracking {
    Futoshiki futoshiki;
    Skyscrapper skyscrapper;
    List<Cell> cellsOrder;
    int currentIndex;
    int numberOfCalls;
    int numberOfSolutions;

    boolean useFirstHeuristic;
    boolean useSecondHeuristic;
    boolean checkForward;
    boolean sky;

    public Backtracking(Futoshiki futoshiki,boolean firstHeuristic,boolean secondHeuristic, boolean checkForward)
    {
        this.futoshiki = futoshiki;
        cellsOrder = new ArrayList<>();
        setCellsOrder();
//        setCellsOrderMostLimited();
        this.currentIndex = 0;
        this.numberOfCalls = 0;
        this.numberOfSolutions = 0;
        this.useFirstHeuristic = firstHeuristic;
        this.useSecondHeuristic = secondHeuristic;
        this.checkForward = checkForward;
        this.sky = false;
    }
    public Backtracking(Skyscrapper skyscrapper,boolean firstHeuristic,boolean secondHeuristic, boolean checkForward)
    {
        this.skyscrapper = skyscrapper;
        cellsOrder = new ArrayList<>();
        setCellsOrderSky();
//        setCellsOrderMostLimited();
        this.currentIndex = 0;
        this.numberOfCalls = 0;
        this.numberOfSolutions = 0;
        this.useFirstHeuristic = firstHeuristic;
        this.useSecondHeuristic = secondHeuristic;
        this.checkForward = checkForward;
        this.sky = true;

    }

    private void setCellsOrder()
    {
        for(int i = 0; i < futoshiki.getBoard().size(); i++)
        {
            for(int j = 0; j < futoshiki.getBoard().size(); j++)
            {
                Cell cell = futoshiki.getBoard().get(i).get(j);
                if(!cell.isSet())
                {
                    cell.setIndex(cellsOrder.size());
                    cellsOrder.add(cell);
                }
            }
        }
    }
    private void setCellsOrderSky()
    {
        for(int i = 0; i < skyscrapper.getBoard().size(); i++)
        {
            for(int j = 0; j < skyscrapper.getBoard().size(); j++)
            {
                Cell cell = skyscrapper.getBoard().get(i).get(j);
                if(!cell.isSet())
                {
                    cell.setIndex(cellsOrder.size());
                    cellsOrder.add(cell);
                }
            }
        }
    }
    private Cell findNextCell() // znalezienie najbardziej ograniczającej z najbardziej ograniczonych
    {
        int minValue = Integer.MAX_VALUE;
        int maxNumber = 0;
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
                        maxNumber = cell.getNumberOfDependents();
                    }
                    else if(cell.getLeftInDomain().size() == minValue)
                    {
                        if(cell.getNumberOfDependents()>maxNumber)
                        {
                            selectedCell = cell;
                            maxNumber = cell.getNumberOfDependents();
                        }
                    }
                }
            }
        }
        return  selectedCell;
    }
    private Cell findNextCellSky() // znalezienie najbardziej ograniczającej z najbardziej ograniczonych
    {
        int minValue = Integer.MAX_VALUE;
        int maxNumber = 0;
        Cell selectedCell = null;
        for(int i = 0; i < skyscrapper.getBoard().size(); i++)
        {
            for(int j = 0; j < skyscrapper.getBoard().size(); j++)
            {
                if(!skyscrapper.getBoard().get(i).get(j).isSet())
                {
                    Cell cell = skyscrapper.getBoard().get(i).get(j);
                    if(cell.getLeftInDomain().size() < minValue)
                    {
                        minValue = cell.getLeftInDomain().size();
                        selectedCell = cell;
                        maxNumber = cell.getNumberOfDependents();
                    }
                    else if(cell.getLeftInDomain().size() == minValue)
                    {
                        if(cell.getNumberOfDependents()>maxNumber)
                        {
                            selectedCell = cell;
                            maxNumber = cell.getNumberOfDependents();
                        }
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

    public boolean runAlgorithm()
    {
        // initialize algorithm
        numberOfCalls = 0;
        currentIndex = -1;

        boolean result = runStep( null );

        return result;
    }
    public boolean runAlgorithmSky()
    {
        // initialize algorithm
        numberOfCalls = 0;
        currentIndex = -1;

        boolean result = runStepSky( null );

        return result;
    }

    // ustala nastepną komorkę dla danego stanu tablicy
    //
    public Cell chooseNextCell( Cell prevCell )
    {
        if (useFirstHeuristic)
        {
            //return new Cell(-1);// todo
            return findNextCell();
        }
        else
        {
            int nextIndex = (prevCell != null) ? prevCell.getIndex() + 1 : 0;
            return (nextIndex < cellsOrder.size()) ? cellsOrder.get(nextIndex) : null;
        }
    }
    public Cell chooseNextCellSky( Cell prevCell )
    {
        if (useFirstHeuristic)
        {
            //return new Cell(-1);// todo
            return findNextCellSky();
        }
        else
        {
            int nextIndex = (prevCell != null) ? prevCell.getIndex() + 1 : 0;
            return (nextIndex < cellsOrder.size()) ? cellsOrder.get(nextIndex) : null;
        }
    }

    public List<Integer> getPossibleValues( Cell cell )
    {
        if (useSecondHeuristic)
        {
         // todo
          //  return new ArrayList<Integer>();
            return sortDomain(cell);
        }
        else
        {
            return cell.getLeftInDomain();
        }
    }

    public boolean runStep( Cell prevCell )
    {
        numberOfCalls++;
        // Choose next cell
        Cell nextCell = chooseNextCell( prevCell );

        // Board is full
        if (nextCell == null)
        {
            numberOfSolutions++;
            return false;
        }

        currentIndex = nextCell.getIndex();

        // Evaluate possible values
        List<Integer> possibleValues = getPossibleValues(nextCell);

        // For each
        for (int i =0; i < possibleValues.size(); ++i)
        {
            nextCell.setValue(possibleValues.get(i));
            boolean result = nextCell.updateConstrainedDomains();
            if(sky)
            {
                if(!nextCell.checkSkyConstraints())
                {
                    continue;
                }
            }
            if (checkForward && !result)
            {
                continue;
            }
            if (runStep( nextCell ))
                return true;
            else
                continue;
        }

        nextCell.reset();
        nextCell.updateConstrainedDomains();
        return false;
    }
    public boolean runStepSky( Cell prevCell )
    {
        numberOfCalls++;
        // Choose next cell
        Cell nextCell = chooseNextCellSky( prevCell );

        // Board is full
        if (nextCell == null)
        {
            numberOfSolutions++;
            return  false;
        }
        currentIndex = nextCell.getIndex();

        // Evaluate possible values
        List<Integer> possibleValues = getPossibleValues(nextCell);

        // For each
        for (int i =0; i < possibleValues.size(); ++i)
        {
            nextCell.setValue(possibleValues.get(i));
            boolean result = nextCell.updateConstrainedDomains();
                if(!nextCell.checkSkyConstraints())
                {
                    continue;
                }

            if (checkForward && !result)
            {
                continue;
            }
            if (runStepSky( nextCell ))
                return true;
            else
                continue;
        }

        nextCell.reset();
        nextCell.updateConstrainedDomains();
        return false;
    }
}
