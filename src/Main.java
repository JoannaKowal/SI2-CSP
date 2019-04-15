import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        Futoshiki futoshiki = new Futoshiki("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_futo_9_0 .txt");
//        Skyscrapper skyscrapper = new Skyscrapper("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_futo_5_4.txt");
        Backtracking backtracking = new Backtracking(futoshiki,false,false, true);
//        Backtracking backtracking = new Backtracking(skyscrapper, false, false, true);
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
            System.out.println("Liczba wywołań: " + backtracking.numberOfCalls);
            System.out.println("Liczba rozwiązań: " + backtracking.numberOfSolutions);
        }
        double endTime = System.currentTimeMillis();
        double duration = (endTime - startTime)/1000;
        DecimalFormat df = new DecimalFormat("#.########");
        df.format(duration);
        System.out.println(duration);

    }


}
