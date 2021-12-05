package models;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.*;
import java.util.function.Function;

public class Station implements Comparable<Station> {
    private final int stn;
    private final String name;
    private NavigableMap<LocalDate, Measurement> measurements;

    public Station(int id, String name) {
        this.stn = id;
        this.name = name;
        // TODO initialize the measurements data structure with a suitable implementation class.
        this.measurements = new TreeMap<>(); // TODO decide what data structure to use
    }

    public Collection<Measurement> getMeasurements() {
        // TODO return the measurements of this station

        return new ArrayList<>(this.measurements.values());
    }

    public int getStn() {
        return stn;
    }

    public String getName() {
        return name;
    }

    /**
     * import station number and name from a text line
     * @param textLine
     * @return  a new Station instance for this data
     *          or null if the data format does not comply
     */
    public static Station fromLine(String textLine) {
        String[] fields = textLine.split(",");
        if (fields.length < 2) return null;
        try {
            return new Station(Integer.valueOf(fields[0].trim()), fields[1].trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Add a collection of new measurements to this station.
     * Measurements that are not related to this station
     * and measurements with a duplicate date shall be ignored and not added
     * @param newMeasurements
     * @return  the nett number of measurements which have been added.
     */
    public int addMeasurements(Collection<Measurement> newMeasurements) {
        int oldSize = this.getMeasurements().size();

        // TODO add all newMeasurements to the station (Yaël)
        //  ignore those who are not related to this station and entries with a duplicate date.

        newMeasurements.stream()
                .filter(measurement -> measurement.getStation().equals(this))
                .filter(measurement -> !measurements.containsKey(measurement.getDate()))
                .forEach(measurement -> measurements.put(measurement.getDate(), measurement));


        return this.getMeasurements().size() - oldSize;
    }

    /**
     * calculates the all-time maximum temperature for this station
     * @return  the maximum temperature ever measured at this station
     *          returns Double.NaN when no valid measurements are available
     */
    public double allTimeMaxTemperature() {
        // TODO calculate the maximum wind gust speed across all valid measurements (Jip)

        return this.measurements.values()
                .stream()
                .mapToDouble(Measurement::getMaxWindGust)
                .max()
                .orElse(Double.NaN);
    }

    /**
     * @return  the date of the first day of a measurement for this station
     *          returns Optional.empty() if no measurements are available
     */
    public Optional<LocalDate> firstDayOfMeasurement() {
        // TODO get the date of the first measurement at this station (Yaël)

        return measurements
                .keySet()
                .stream()
                .sorted(Comparator.naturalOrder())
                .findFirst();
    }

    /**
     * calculates the number of valid values of the data field that is specified by the mapper
     * invalid or empty values should be represented by Double.NaN
     * this method can be used to check on different types of measurements each with their own mapper
     * @param mapper    the getter method of the data field to be checked.
     * @return          the number of valid values found
     */
    public int numValidValues(Function<Measurement,Double> mapper) {
        // TODO count the number of valid values that can be accessed in the measurements collection (Jip)
        //  by means of the mapper access function
        double numberOfValidValuesFound = this.measurements.values()
                .stream()
                .mapToDouble(mapper::apply)
                .sum();

        return (int) numberOfValidValuesFound;
    }

    /**
     * calculates the total precipitation at this station
     * across the time period between startDate and endDate (inclusive)
     * @param startDate     the start date of the period of accumulation (inclusive)
     * @param endDate       the end date of the period of accumulation (inclusive)
     * @return              the total precipitation value across the period
     *                      0.0 if no measurements have been made in this period.
     */
    public double totalPrecipitationBetween(LocalDate startDate, LocalDate endDate) {
        // TODO calculate and return the total precipitation across the given period (Yaël)
        //  use the 'subMap' method to only process the measurements within the given period


        return measurements.subMap(startDate, endDate).values().stream().mapToDouble(Measurement::getPrecipitation).sum();
    }

    /**
     * calculates the average of all valid measurements of the quantity selected by the mapper function
     * across the time period between startDate and endDate (inclusive)
     * @param startDate     the start date of the period of averaging (inclusive)
     * @param endDate       the end date of the period of averaging (inclusive)
     * @param mapper        a getter method that obtains the double value from a measurement instance to be averaged
     * @return              the average of all valid values of the selected quantity across the period
     *                      Double.NaN if no valid measurements are available from this period.
     */
    public double averageBetween(LocalDate startDate, LocalDate endDate, Function<Measurement,Double> mapper) {
        Map<LocalDate, Measurement> measurementsWithinTheGivenPeriod = this.measurements.subMap(startDate, endDate);

        return measurementsWithinTheGivenPeriod.values()
                .stream()
                .mapToDouble(mapper::apply)
                .average()
                .orElse(Double.NaN);
    }

    public int getMeasurementsSize() {
        return this.measurements.size();
    }

    // TODO any other methods required to make it work

    @Override
    public int compareTo(Station o) {
        return Integer.compare(this.getStn(), o.getStn());
    }

    @Override
    public String toString() {
        return String.format("%d/%s", this.stn, this.name);
    }
}
