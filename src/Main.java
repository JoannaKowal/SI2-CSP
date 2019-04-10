import java.util.List;

public class Main {
    public static void main(String[] args) {
        Futoshiki futoshiki = new Futoshiki("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_futo_8_0.txt");
        Backtracking backtracking = new Backtracking(futoshiki);
        if(backtracking.run())
        {
            for(int i = 0; i < futoshiki.getBoard().size(); i++)
            {
                for(int j = 0; j < futoshiki.getBoard().size(); j++)
                {
                    System.out.print(futoshiki.getBoard().get(i).get(j).getValue());
                }
                System.out.println();
            }

        }
        else
        {
            System.out.println("Nie znaleziono rozwiazania");
        }
    }


}
