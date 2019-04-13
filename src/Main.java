public class Main {
    public static void main(String[] args) {
       // Futoshiki futoshiki = new Futoshiki("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_futo_8_2.txt");
        Skyscrapper skyscrapper = new Skyscrapper("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_sky_5_4.txt");
      //  Backtracking backtracking = new Backtracking(futoshiki,true,false, true);
        Backtracking backtracking = new Backtracking(skyscrapper, false, false, true);
        double startTime = System.currentTimeMillis();
        if(backtracking.runAlgorithmSky())
        {
            for(int i = 0; i < skyscrapper.getBoard().size(); i++)
            {
                for(int j = 0; j < skyscrapper.getBoard().size(); j++)
                {
                    System.out.print(skyscrapper.getBoard().get(i).get(j).getValue());
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
        System.out.println(duration);

    }


}
