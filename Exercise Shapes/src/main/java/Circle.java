import java.awt.*;
import java.util.Objects;

/**
 * This class creates the object of a Circle.
 *
 * @author Yaël de Vries
 */

public class Circle extends Shape
{
    private final double radius;

    public Circle(Color colour, double radius)
    {
        super(colour);
        this.radius = radius;
    }

    @Override
    public double getArea()
    {
        return Math.PI * Math.pow(radius,2);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Circle))
            return false;
        if (!super.equals(o))
            return false;
        Circle circle = (Circle) o;
        return Double.compare(circle.radius, radius) == 0;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
