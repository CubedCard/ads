package graphs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class <description of functionality>
 *
 * @author jipderksen
 */
public class OurDirectedGraphTest {

    Restaurant schots, ebeling, loetje, vrijdag, wijmpje, gieter;
    DirectedGraph<Restaurant, Integer> amsterdam = new DirectedGraph<>();

    @BeforeEach
    void setup() {
        schots = this.amsterdam.addOrGetVertex(new Restaurant("Eetcafe Schotsheuvel"));
        ebeling = this.amsterdam.addOrGetVertex(new Restaurant("de Ebeling"));
        this.amsterdam.addConnection(schots, ebeling, 120);
        loetje = this.amsterdam.addOrGetVertex(new Restaurant("Loetje"));
        this.amsterdam.addConnection(loetje, ebeling, 200);
        this.amsterdam.addConnection(loetje, schots, 190);
        vrijdag = this.amsterdam.addOrGetVertex(new Restaurant("Cafe Vrijdag"));
        this.amsterdam.addConnection(vrijdag, ebeling, 50);
        wijmpje = this.amsterdam.addOrGetVertex(new Restaurant("Wijmpje Beukers"));
        this.amsterdam.addConnection(wijmpje, schots, 60);
        gieter = this.amsterdam.addOrGetVertex(new Restaurant("Cafe De Gieter"));
    }

    @AfterEach
    void checkRepresentationInvariants() {
        assertEquals(6, amsterdam.getNumVertices());
        assertEquals(10, amsterdam.getNumEdges());
        for (Restaurant from : amsterdam.getVertices()) {
            for (Restaurant to : amsterdam.getNeighbours(from)) {
                assertSame(amsterdam.getEdge(from, to), amsterdam.getEdge(to, from),
                        "Border between two countries should be the same object instance");
            }
        }
    }

    @Test
    void checkGetNeighbours() {
        assertEquals(1, this.amsterdam.getNeighbours("Wijmpje Beukers").size());
    }

    @Test
    void checkGetEdge() {
        assertEquals(120, this.amsterdam.getEdge("Eetcafe Schotsheuvel", "de Ebeling"));
    }

    @Test
    void checkRemoveUnconnectedVertices() {
        this.amsterdam.removeUnconnectedVertices();
        assertEquals(5, amsterdam.getNumVertices());

        this.amsterdam.addOrGetVertex(gieter);
    }

    @Test
    void checkToString() {
        String stringRepresentationOfAmsterdam = this.amsterdam.toString();
        assertTrue(stringRepresentationOfAmsterdam.contains("Eetcafe Schotsheuvel"));
        assertTrue(stringRepresentationOfAmsterdam.contains("de Ebeling"));
        assertTrue(stringRepresentationOfAmsterdam.contains("Loetje"));
        assertTrue(stringRepresentationOfAmsterdam.contains("Cafe Vrijdag"));
        assertTrue(stringRepresentationOfAmsterdam.contains("Wijmpje Beukers"));
        assertTrue(stringRepresentationOfAmsterdam.contains("Cafe De Gieter"));
    }

    @Test
    void checkToStringDGPath() {
        DirectedGraph<Restaurant, Integer>.DGPath path = amsterdam.dijkstraShortestPath("Wijmpje Beukers", "de " +
                "Ebeling", r -> 3.0);
        assertNotNull(path);
        assertSame(amsterdam.getVertexById("Wijmpje Beukers"), path.getVertices().peek());
        assertEquals(6.0, path.getTotalWeight(), 0.0001);
        assertEquals(path.getTotalWeight(), 3.0 * (path.getVertices().size() - 1), 0.0001);
        assertTrue(path.getVisited().size() >= path.getVertices().size());

        assertTrue(path.toString().contains("(Wijmpje Beukers, Eetcafe Schotsheuvel, de Ebeling)"));
    }

}
