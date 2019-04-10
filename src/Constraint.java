import java.util.List;

public interface Constraint {
    public List<Cell> getCells();
    public boolean isSatisfied();
}
