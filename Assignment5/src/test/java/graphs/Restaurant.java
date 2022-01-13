package graphs;

/**
 * This class is an abstract representation of a restaurant
 *
 * @author jipderksen
 */
public class Restaurant implements Identifiable {
    private String name;

    public Restaurant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Restaurant restaurant = (Restaurant) o;
        return name.equals(restaurant.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
