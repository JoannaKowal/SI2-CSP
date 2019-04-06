import java.util.List;

public class Main {
    public static void main(String[] args) {
        Futoshiki futoshiki = new Futoshiki();
        futoshiki.load("C:\\Users\\Asiek\\Projekty\\SI2-CSP\\src\\test_futo_4_0.txt");
        for (List<Cell> cells : futoshiki.getBoard()) {
            System.out.println();
            for (Cell cell : cells) {
                System.out.print(cell.getValue() + ", ");
            }
        }
    }


}
