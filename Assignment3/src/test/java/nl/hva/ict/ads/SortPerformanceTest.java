package nl.hva.ict.ads;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class SortPerformanceTest {
    private final double MAX_NUMBER_OF_RECORDS = 5E6;
    private long seed = 1L;
    protected Sorter<Archer> sorter = new ArcherSorter();
    ChampionSelector championSelector = new ChampionSelector(seed);
    protected List<Archer> archers = new ArrayList<>(championSelector.enrollArchers((int) this.MAX_NUMBER_OF_RECORDS));
    protected Comparator<Archer> scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;
    private int numberOfArchers;
    private double time;

    private static final List<Double> collectionSortTimes = new ArrayList<>();
    private static final List<Double> selInsSortTimes = new ArrayList<>();
    private static final List<Double> quickSortTimes = new ArrayList<>();
    private static final List<Double> topHeapSortTimes = new ArrayList<>();

    @BeforeEach
    void setup() {
        System.gc();
        this.seed++;
        this.numberOfArchers = 100;
        this.time = 0;
    }

    @AfterEach
    void printResults() {
        System.out.println();
    }

    @AfterAll
    static void print() {
        for (int i = 0; i < Math.max((Math.max(collectionSortTimes.size(), selInsSortTimes.size())),
                (Math.max(quickSortTimes.size(), topHeapSortTimes.size()))); i++) {
            if (i < collectionSortTimes.size()) System.out.printf("%.2f;", collectionSortTimes.get(i));
            else System.out.print(";");
            if (i < selInsSortTimes.size()) System.out.printf("%.2f;", selInsSortTimes.get(i));
            else System.out.print(";");
            if (i < quickSortTimes.size()) System.out.printf("%.2f;", quickSortTimes.get(i));
            else System.out.print(";");
            if (i < topHeapSortTimes.size()) System.out.printf("%.2f;", topHeapSortTimes.get(i));
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
            collectionSortTimes.add(this.time);
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
            selInsSortTimes.add(this.time);
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
            quickSortTimes.add(this.time);
        } while (condition());
    }

    @RepeatedTest(10)
    void topHeapSortPerformanceTest() {
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0, numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            sorter.topsHeapSort(numberOfArchers, archers1, scoringScheme);
            long end = System.nanoTime();

            this.calculateTime(start, end);
            topHeapSortTimes.add(this.time);
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
