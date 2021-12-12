import models.Measurement;
import models.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the Measurement class
 *
 * @author jipderksen
 */
public class OurMeasurementTest {
    Station deBilt, maastricht;
    Map<Integer,Station> stations;

    @BeforeEach
    private void setup() {
        deBilt = new Station(260, "De Bilt");
        maastricht = new Station(380, "Maastricht");
        stations = Map.of(260, deBilt, 380, maastricht);
    }

    @Test
    public void cantConvertACorruptTextLineToAMeasurement() {
        // Measurement with an invalid date field
        Measurement measurement1 = Measurement.fromLine("  380,test,   95,   36,   36,   77,   12,    5,   24,     , " +
                "    ,  -23,  -59,     ,   26,     ,     ,     ,   40,   50,     ,     ,     ,     ,     ,10218,10251,     ,10196,     ,     ,     ,     ,     ,     ,   47,     ,     ,     ,     ,     ", stations);
        // Measurement with an invalid stn
        Measurement measurement2 = Measurement.fromLine("  ll,19750914,  204,   87,   87,  123,   14,   51,   20,  " +
                "185,   14,  141,  113,   24,  176,   14,  100,     ,   14,   11,  944,    5,    6,    6,     , 9991,10046,     , 9959,     ,   70,    1,   82,    2,    7,   75,   94,    1,   56,   14,   15", stations);
        assertNull(measurement1);
        assertNull(measurement2);
    }
}
