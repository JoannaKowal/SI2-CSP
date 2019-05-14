import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Skyscrapper extends Game{

    private List<List<Cell>> constraints;
    private int size;

    public Skyscrapper(String fileName) {
        this.constraints = new ArrayList<>();
        load(fileName);


    }


    public void load(String fileName)
    {
        try{
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            try {
                this.size = Integer.valueOf(bufferedReader.readLine());
                createBoard();
            } catch(IOException exception){}

            int[][] constraints = new int[size][size];
           for(int i = 0; i < 4; i++)
            {
                String[] cells = readConstraints(bufferedReader);
                for(int j = 1; j < cells.length; j++)
                {
                    int value = Integer.valueOf(cells[j]);
                    constraints[i][j - 1] = value;
                }
            }
            setCellsConstraints(constraints);
            setDomain();
            setConstraints();
            fixDomains();
        }catch(FileNotFoundException exception){}

    }
    private String[] readConstraints(BufferedReader br)
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
    private void setCellsConstraints(int[][] constraints)
    {
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                List<Integer> cellConstraints = new ArrayList<>();
                Cell cell = board.get(i).get(j);
                cellConstraints.add(constraints[0][j]);
                cellConstraints.add(constraints[1][j]);
                cellConstraints.add(constraints[2][i]);
                cellConstraints.add(constraints[3][i]);
                SkyscrapperConstraint constraint = new SkyscrapperConstraint();
                constraint.setConstraints(cellConstraints);
                cell.getSkyConstraints().add(constraint);
            }
        }
    }
    private void createBoard()
    {
        for (int i = 0; i < size; i++)
        {
            List<Cell> row = new ArrayList<>();
            for(int j = 0; j < size; j++)
            {
                Cell cell = new Cell(0);
                row.add(cell);
            }
            this.board.add(row);
        }
    }
    private void fixDomains()
    {
        for (List<Cell> cells : board) {
            for (Cell cell : cells) {
                List <Integer> skyConstraints =  cell.getSkyConstraints().get(0).getConstraints();
                if(skyConstraints.get(2)==1){
                    Cell borderCell = cell.getConstraints().get(0).getCells().get(0);
                    borderCell.setValue(size);
                }
                if(skyConstraints.get(3)==1){
                    Cell borderCell = cell.getConstraints().get(0).getCells().get(size-1);
                    borderCell.setValue(size);
                }
                if(skyConstraints.get(0)==1){
                    Cell borderCell = cell.getConstraints().get(1).getCells().get(0);
                    borderCell.setValue(size);
                }
                if(skyConstraints.get(1)==1){
                    Cell borderCell = cell.getConstraints().get(1).getCells().get(size-1);
                    borderCell.setValue(size);
                }

            }
        }

    }
    private void setDomain()
    {
        for (List<Cell> cells : board) {
            for (Cell cell : cells) {
                for(int i = 1; i <= this.size; i++)
                {
                    cell.getWholeDomain().add(i);
                    cell.getLeftInDomain().add(i);
                }
            }
        }

    }
    private void setConstraints() {
        for (int i = 0; i < board.size(); i++) //wczytanie wierszy
        {
            Constraint horizontalConstraint = new DistinctionConstraint();
            List<Cell> row = board.get(i);
            horizontalConstraint.getCells().addAll(row);
            for (int j = 0; j < row.size(); j++) {

                row.get(j).addConstraint(horizontalConstraint);
            }
        }
        for (int i = 0; i < board.size(); i++)//wczytanie kolumn
        {
            Constraint verticalConstraint = new DistinctionConstraint();
            List<Cell> column = new ArrayList<>();
            verticalConstraint.getCells().addAll(getColumn(i));
            for (int j = 0; j < board.size(); j++) {
                getColumn(i).get(j).addConstraint(verticalConstraint);
            }
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
}
