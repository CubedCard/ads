import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * This class creates the grid and adds / removes the shapes.
 * It can also display the grid or return the total area of the shapes.
 *
 * @author Yaël de Vries
 */

public class Board<S extends Shape> implements Iterable<S>
{
    private final int boardSize;

    private final S grid[][];

    public Board(int boardSize)
    {
        this.boardSize = boardSize;
        grid = (S[][]) new Shape[boardSize][boardSize];
    }

    public boolean add(S shape, int x, int y)
    {
        if (x >= boardSize || x < 0 || y >= boardSize || y < 0)
            throw new IndexOutOfBoundsException(String.format("Position %d,%d is not available on a board of size %d", x, y, boardSize));
        if (grid[x][y] == null)
        {
            grid[x][y] = shape;
            return true;
        }
        return false;
    }

    public S remove(int x, int y)
    {
        if (x >= boardSize || x < 0 || y >= boardSize || y < 0)
            throw new IndexOutOfBoundsException(String.format("Position %d,%d is not available on a board of size %d", x, y, boardSize));
        if (grid[x][y] != null)
        {
            S shape = grid[x][y];
            grid[x][y] = null;
            return shape;
        }
        return grid[x][y];
    }

    public S[][] getGrid()
    {
        return grid;
    }

    public double getShapesArea()
    {
        double totalArea = 0;

        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                if (grid[i][j] != null)
                {
                    totalArea += grid[i][j].getArea();
                }
            }
        }
        return totalArea;
    }

    @Override
    public Iterator<S> iterator()
    {
        Iterable<S> shape = (Iterable<S>) Arrays.asList(getGrid());
        return shape.iterator();
    }

    @Override
    public void forEach(Consumer<? super S> action)
    {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<S> spliterator()
    {
        return Iterable.super.spliterator();
    }
}
