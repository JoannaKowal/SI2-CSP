import java.util.ArrayList;
import java.util.List;

public class Game
{
    protected List<List<Cell>> board;

    public Game()
    {
        this.board = new ArrayList<>();
    }

    public List<List<Cell>> getBoard() {
        return board;
    }
}
