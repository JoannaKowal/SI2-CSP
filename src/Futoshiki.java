import com.sun.deploy.util.ArrayUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Futoshiki {
    private List<List<Cell>> board;
    private List<List<Constraint>> constraints;

    public Futoshiki() {
        this.board = new ArrayList<>();
        this.constraints = new ArrayList<>();
    }

    public List<List<Cell>> getBoard() {
        return board;
    }

    public void load(String fileName)
    {
        try{
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            try {
                bufferedReader.readLine();
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
            while(constraints[0] != ' ')
            {
                List<Constraint> row = new ArrayList<>();
                int firstRow = constraints[0] - 'A';
                int secondRow = constraints[2] - 'A';
                int firstColumn = Character.getNumericValue(constraints[1]);
                int secondColumn = Character.getNumericValue(constraints[3]);
                Constraint firstConstraint = new Constraint(firstRow, firstColumn);
                Constraint secondConstraint = new Constraint(secondRow, secondColumn);
                row.add(firstConstraint);
                row.add(secondConstraint);
                this.constraints.add(row);
                constraints = readConstraints(bufferedReader);
            }
        }catch(FileNotFoundException exception){}
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

            if(line == null)
            {
                return parts;
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
}
