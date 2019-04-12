public class Main {
    public static void main(String[] args) {
        Futoshiki futoshiki = new Futoshiki("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_futo_7_0.txt");
        Backtracking backtracking = new Backtracking(futoshiki);
        double startTime = System.currentTimeMillis();
        if(backtracking.runAlgorithm())
        {
            for(int i = 0; i < futoshiki.getBoard().size(); i++)
            {
                for(int j = 0; j < futoshiki.getBoard().size(); j++)
                {
                    System.out.print(futoshiki.getBoard().get(i).get(j).getValue());
                }
                System.out.println();
            }
            System.out.println("Liczba wywołań: " + backtracking.numberOfCalls);

        }
        else
        {
            System.out.println("Nie znaleziono rozwiazania");
        }
        double endTime = System.currentTimeMillis();
        double duration = (endTime - startTime)/1000;
        System.out.println(duration);
    }


}
