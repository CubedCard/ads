import java.awt.*;

/**
 * This class creates the object of a rectangle.
 *
 * @author Yaël de Vries
 */

public class Rectangle extends Shape
{
    private final int width;
    private final int length;

    public Rectangle(Color colour, int width, int length)
    {
        super(colour);
        this.width = width;
        this.length = length;
    }

    public int getWidth()
    {
       return width;
    }

    public int getLength()
    {
        return length;
    }

    @Override
    public double getArea()
    {
        return width * length;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!super.equals(o))
            return false;
        if (o instanceof Rectangle)
        {
            Rectangle before = (Rectangle) o;

            return before.getLength() == length && before.getWidth() == width;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
