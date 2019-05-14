import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        Futoshiki futoshiki = new Futoshiki("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_futo_8_0.txt");

//        if (!futoshiki.isLoaded())
//        {
//            System.out.println("Failed to load file.");
//            return;
//        }
//        Skyscrapper skyscrapper = new Skyscrapper("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_sky_6_3.txt");
        Backtracking backtracking = new Backtracking(futoshiki,false,true, false);
//        Backtracking backtracking = new Backtracking(skyscrapper, true, false, false);
        double startTime = System.currentTimeMillis();
        if(backtracking.runAlgorithm())
        {


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
