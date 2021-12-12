import models.ClimateTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the ClimateTracker class
 *
 * @author jipderksen
 */
public class OurClimateTrackerTest {

    ClimateTracker climateTracker;

    @BeforeEach
    private void setup() {
        climateTracker = new ClimateTracker();
    }

    @Test void checkColdestYearNotFound() {
        assertEquals(-1, this.climateTracker.coldestYear());
    }
}
