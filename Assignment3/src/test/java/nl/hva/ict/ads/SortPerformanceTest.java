package nl.hva.ict.ads;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.*;

class SortPerformanceTest {
    private static final int NUMBER_OF_TIMES_TESTED = 10;
    private final double MAX_NUMBER_OF_RECORDS = 5E6;
    private long seed = 1L;
    protected Sorter<Archer> sorter = new ArcherSorter();
    ChampionSelector championSelector = new ChampionSelector(seed);
    protected List<Archer> archers = new ArrayList<>(championSelector.enrollArchers((int) this.MAX_NUMBER_OF_RECORDS));
    protected Comparator<Archer> scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;
    private int numberOfArchers;
    private double time;
    private int numberOfTimes;

    private static final Map<Integer, Double> collectionSortTimes = new HashMap<>();
    private static final Map<Integer, Double> selInsSortTimes = new HashMap<>();
    private static final Map<Integer, Double> quickSortTimes = new HashMap<>();
    private static final Map<Integer, Double> topHeapSortTimes = new HashMap<>();

    @BeforeEach
    void setup() {
        System.gc();
        this.seed++;
        Names.reSeed(new Random(seed).nextLong());
        this.numberOfArchers = 50;
        this.time = 0;
        this.numberOfTimes = 0;
    }

    @AfterEach
    void printResults() {
        System.out.println();
    }

    @AfterAll
    static void print() {
        for (int i = 0; i < Math.max((Math.max(collectionSortTimes.size(), selInsSortTimes.size())),
                (Math.max(quickSortTimes.size(), topHeapSortTimes.size()))); i++) {
            if (i < collectionSortTimes.size()) {
                collectionSortTimes.put(i, collectionSortTimes.get(i) / NUMBER_OF_TIMES_TESTED);
                System.out.printf("%.2f;", collectionSortTimes.get(i));
            }
            else System.out.print(";");
            if (i < selInsSortTimes.size()) {
                selInsSortTimes.put(i, selInsSortTimes.get(i) / NUMBER_OF_TIMES_TESTED);
                System.out.printf("%.2f;", selInsSortTimes.get(i));
            }
            else System.out.print(";");
            if (i < quickSortTimes.size()) {
                quickSortTimes.put(i, quickSortTimes.get(i) / NUMBER_OF_TIMES_TESTED);
                System.out.printf("%.2f;", quickSortTimes.get(i));
            }
            else System.out.print(";");
            if (i < topHeapSortTimes.size()) {
                topHeapSortTimes.put(i, topHeapSortTimes.get(i) / NUMBER_OF_TIMES_TESTED);
                System.out.printf("%.2f;", topHeapSortTimes.get(i));
            }
            else System.out.print(";");
            System.out.println();
        }
    }

    @RepeatedTest(10)
    void collectionSortPerformanceTest() {
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0, numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            archers1.sort(scoringScheme);
            long end = System.nanoTime();

            this.calculateTime(start, end);
            if (collectionSortTimes.get(numberOfTimes) != null) this.time += collectionSortTimes.get(numberOfTimes);
            collectionSortTimes.put(numberOfTimes, this.time);
            numberOfTimes++;
        } while (condition());
    }

    @RepeatedTest(10)
    void selInsSortPerformanceTest() {
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0, numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            sorter.selInsSort(archers1, scoringScheme);
            long end = System.nanoTime();

            this.calculateTime(start, end);
            if (selInsSortTimes.get(numberOfTimes) != null) this.time += selInsSortTimes.get(numberOfTimes);
            selInsSortTimes.put(numberOfTimes, this.time);
            numberOfTimes++;
        } while (condition());
    }

    @RepeatedTest(10)
    void quickSortPerformanceTest() {
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0, numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            sorter.quickSort(archers1, scoringScheme);
            long end = System.nanoTime();

            this.calculateTime(start, end);
            if (quickSortTimes.get(numberOfTimes) != null) this.time += quickSortTimes.get(numberOfTimes);
            quickSortTimes.put(numberOfTimes, this.time);
            numberOfTimes++;
        } while (condition());
    }

    @RepeatedTest(10)
    void topHeapSortPerformanceTest() {
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0, numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            sorter.topsHeapSort(10, archers1, scoringScheme);
            long end = System.nanoTime();

            this.calculateTime(start, end);
            if (topHeapSortTimes.get(numberOfTimes) != null) this.time += topHeapSortTimes.get(numberOfTimes);
            topHeapSortTimes.put(numberOfTimes, this.time);
            numberOfTimes++;
        } while (condition());
    }

    private boolean condition() {
        double MAX_TIME = 20000;
        return numberOfArchers < this.MAX_NUMBER_OF_RECORDS && this.time < MAX_TIME;
    }

    private void doubleNumberOfArchers() {
        this.numberOfArchers = this.numberOfArchers * 2;
        if (numberOfArchers > this.MAX_NUMBER_OF_RECORDS) numberOfArchers = (int) this.MAX_NUMBER_OF_RECORDS;
    }

    private void calculateTime(long start, long end) {
        this.time = (end - start) / 1E6;
        System.out.printf("Time taken: %.2f ms\nNumber Of archers: %d\n", this.time, numberOfArchers);
    }

}
