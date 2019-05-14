

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Futoshiki  extends Game{
    private List<List<Cell>> constraints;
    private int size;

    public Futoshiki(String fileName) {
        this.constraints = new ArrayList<>();
        load(fileName);

    }

    public boolean isLoaded() { return !getBoard().isEmpty(); }
    public void load(String fileName)
    {
        try{
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            try {
                this.size = Integer.valueOf(bufferedReader.readLine());
                bufferedReader.readLine();
            } catch(IOException exception){}
            String[] cells = readBoard(bufferedReader);
            while(!(cells[0].equals("REL:")))
            {
                List<Cell> row = new ArrayList<>();
                for(int i = 0; i < cells.length; i++)
                {
                    int cellValue = Integer.valueOf(cells[i]);
                    Cell cell = new Cell(cellValue);
                    row.add(cell);
                }
                this.board.add(row);
                cells = (readBoard(bufferedReader));
            }
            char[] constraints = readConstraints(bufferedReader);
            while(constraints != null)
            {
                List<Cell> row = new ArrayList<>();
                int firstRow = constraints[0] - 'A';
                int secondRow = constraints[2] - 'A';
                int firstColumn = Character.getNumericValue(constraints[1]) - 1;
                int secondColumn = Character.getNumericValue(constraints[3]) - 1;
                Cell firstCell = board.get(firstRow).get(firstColumn);
                Cell secondCell = board.get(secondRow).get(secondColumn);
                row.add(firstCell);
                row.add(secondCell);
                this.constraints.add(row);
                constraints = readConstraints(bufferedReader);
            }
            setConstraints();
            setDomain();

            for(int i = 0; i < board.size(); i++) // ustawienie leftInDomain
            {
                for(int j = 0; j < board.size(); j++)
                {
                    if(!board.get(i).get(j).isSet())
                    {
                        for(int k = 0; k < size; k++)
                        {
                            board.get(i).get(j).getLeftInDomain().add(board.get(i).get(j).getWholeDomain().get(k));
                        }
                    }

                }
            }
            adjustDomains();
            for(int i = 0; i < board.size(); i++) // ustawienie leftInDomain
            {
                for (int j = 0; j < board.size(); j++) {
                    Cell cell = board.get(i).get(j);

                    if (!cell.isSet()) {
                        cell.getWholeDomain().clear();
                        cell.getWholeDomain().addAll(cell.getLeftInDomain());
                    }
                }
            }
        }catch(FileNotFoundException exception){
        }

    }
    private String[] readBoard(BufferedReader br)
    {
        String[] parts = {};
        try{
            String line = br.readLine();
            if(line == null)
            {
                return parts;
            }
            parts = line.split(";");

        }catch(IOException exception){}
        return parts;
    }
    private char[] readConstraints(BufferedReader br)
    {
        String[] strings = {};
        char[] parts = {};
        try{
            String line = br.readLine();

            if( (line == null)||(line.length() == 0 ))
            {
                return null;
            }
            strings = line.split(";");
            char[] first = strings[0].toCharArray();
            char[] second = strings[1].toCharArray();
            parts = new char[first.length + second.length];
            for(int i = 0; i < first.length; i++)
            {
                parts[i] = first[i];
            }
            for(int i = first.length; i < first.length + second.length; i++)
            {
                parts[i] = second[i-2];
            }

        }catch(IOException exception){}
        return parts;
    }
    private void setConstraints()
    {
        for (int i = 0; i < board.size(); i++) //wczytanie wierszy
        {
            Constraint horizontalConstraint = new DistinctionConstraint();
            List<Cell> row = board.get(i);
            horizontalConstraint.getCells().addAll(row);
            for (int j = 0; j < row.size(); j++) {

                row.get(j).addConstraint(horizontalConstraint);
            }
        }
        for(int i = 0; i < board.size(); i++)//wczytanie kolumn
        {
            Constraint verticalConstraint = new DistinctionConstraint();
            List<Cell> column = new ArrayList<>();
            verticalConstraint.getCells().addAll(getColumn(i));
            for(int j = 0; j < board.size(); j++)
            {
                getColumn(i).get(j).addConstraint(verticalConstraint);
            }
        }
        for(int i = 0; i < constraints.size(); i++) //wczytanie relacji <
        {
            List<Cell> row = constraints.get(i);
            Constraint constraint = new SmallerThan();
            constraint.getCells().addAll(row);
            row.get(0).addSmallerThan(constraint);
            row.get(1).addSmallerThan(constraint);

        }

    }
    private List<Cell> getColumn(int colNumber)
    {
        List<Cell> column = new ArrayList<>();
        for (int i  = 0; i < board.size(); i++)
        {
            column.add(board.get(i).get(colNumber));
        }
        return  column;
    }
    private void setDomain()
    {
        for (List<Cell> cells : board) {
            for (Cell cell : cells) {
                for(int i = 1; i <= this.size; i++)
                {
                    cell.getWholeDomain().add(i);
                }
            }
        }
    }
    public void adjustDomains()
    {
        for(int i = 0 ; i < board.size(); i++)
        {
            for(int j = 0; j < board.size(); j++)
            {
                if(!board.get(i).get(j).isSet())
                {
                    board.get(i).get(j).updateDomain();
                }
            }
        }
    }
}
