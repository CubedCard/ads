
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ShapeTest
{
    Circle circle1, circle2, circle3;
    Rectangle rectangle1, rectangle2, rectangle3;
    Board board1;
    Board board2;
    int boardSize = 9;
    double totalAreaBoard1;

    @BeforeEach
    void setUp()
    {
        circle1 = new Circle(Color.BLACK,10);
        circle2 = new Circle(Color.BLACK,10);
        circle3 = new Circle(Color.BLUE,10);
        rectangle1 = new Rectangle(Color.BLUE, 4,5);
        rectangle2 = new Rectangle(Color.BLUE, 4,5);
        rectangle3 = new Rectangle(Color.BLUE,2,10);

        board1 = new Board(boardSize);
        for (int i = 0; i < boardSize; i++ )
        {
            for (int j = 0; j < i; j++)
            {
                board1.add(new Rectangle(Color.BLUE, i+1, j+1), i, j);
                board1.add(new Circle(Color.RED, i+j+1), j, i);
            }
        }
        totalAreaBoard1 = 11350.35309237555;
        board2 = new Board(1);
    }

    @Test
    void getArea()
    {
        Assertions.assertEquals(314.1592653589793, circle1.getArea());
        Assertions.assertEquals(20, rectangle1.getArea());
        Assertions.assertEquals(totalAreaBoard1, board1.getShapesArea());
    }

    @Test
    void equals()
    {
        assertEquals(true, circle1.equals(circle2));
        assertEquals(true, rectangle1.equals(rectangle2));
        assertEquals(false,circle1.equals(circle3));
        assertEquals(false,rectangle2.equals(rectangle3));
    }

    @Test
    void testHashCode()
    {
        assertEquals(true, circle1.hashCode() ==circle2.hashCode());
        assertEquals(true, rectangle1.hashCode()==rectangle2.hashCode());
        assertEquals(false,circle1.hashCode()==circle3.hashCode());
    }

    @Test
    void testInsertRemove()
    {
        //when a shape is added it returns true
        assertEquals(true, board1.add(rectangle1,4,4));
        //when a position is occupied it returns false
        assertEquals(false, board1.add(rectangle1,4,4));
        assertEquals(rectangle1, board1.remove(4,4));
        // check invariant of total area board1
        Assertions.assertEquals(11350.35309237555, board1.getShapesArea());
    }
    @Test
    void testAddRemoveOutOfBounds()
    {
        Throwable t;
        t = assertThrows(IndexOutOfBoundsException.class,
                () -> {board1.remove(-1,-1);}
        );
        assertEquals("Position -1,-1 is not available on a board of size "+boardSize, t.getMessage());

        t = assertThrows(IndexOutOfBoundsException.class,
                () -> {board1.add(circle2,-1,0);}
        );
        assertEquals("Position -1,0 is not available on a board of size "+boardSize, t.getMessage());

        t = assertThrows(IndexOutOfBoundsException.class,
                () -> {board2.add(circle2,0,1);}
        );
        assertEquals("Position 0,1 is not available on a board of size 1", t.getMessage());

        t = assertThrows(IndexOutOfBoundsException.class,
                () -> {board2.remove(1,1);}
        );
        assertEquals("Position 1,1 is not available on a board of size 1", t.getMessage());
    }

    @Test
    void testGetGrid()
    {
        //The getGrid method returns a 2D array of Shape
        Shape shapes [][] = board1.getGrid();
        for(int x = 0; x < shapes.length; x++)
        {
            for(int y = 0; y < shapes.length;y++)
            {
                Shape s = shapes[x][y];
                assertEquals(true, s == null || s instanceof Shape);
            }
        }
    }
}