package nl.hva.ict.ads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class SortPerformanceTest {
    private long seed = 1L;
    protected Sorter<Archer> sorter = new ArcherSorter();
    ChampionSelector championSelector = new ChampionSelector(seed);
    protected List<Archer> archers = new ArrayList<>(championSelector.enrollArchers(5000000));
    protected Comparator<Archer> scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;
    private int numberOfArchers;

    @BeforeEach
    void setup() {
        System.gc();
        seed++;
        this.numberOfArchers = 100;
    }

    @AfterEach
    void printResults() {
        System.out.println("hi");
    }

    @Test
    void selInsSortPerformanceTest() {
        double time;
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0,numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            sorter.selInsSort(archers1, scoringScheme);
            long end = System.nanoTime();

            time = ((end - start) / 1E6);
            System.out.printf("Time taken (selInsSort): %.2f ms\nNumber Of archers: %d\n", time, numberOfArchers);
        } while (numberOfArchers < 5E6 && time < 20000);
    }

    @Test
    void quickSortPerformanceTest() {
        double time;
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0,numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            sorter.quickSort(archers1, scoringScheme);
            long end = System.nanoTime();

            time = ((end - start) / 1E6);
            System.out.printf("Time taken (selInsSort): %.2f ms\nNumber Of archers: %d\n", time, numberOfArchers);
        } while (numberOfArchers < 5E6 && time < 20000);
    }

    @Test
    void topHeapSortPerformanceTest() {
        double time;
        do {
            this.doubleNumberOfArchers();
            List<Archer> archers1 = new ArrayList<>(archers.subList(0,numberOfArchers));

            System.gc();

            long start = System.nanoTime();
            sorter.quickSort(archers1, scoringScheme);
            long end = System.nanoTime();

            time = ((end - start) / 1E6);
            System.out.printf("Time taken (selInsSort): %.2f ms\nNumber Of archers: %d\n", time, numberOfArchers);
        } while (numberOfArchers < 5E6 && time < 20000);
    }

    private void doubleNumberOfArchers() {
        this.numberOfArchers = this.numberOfArchers * 2;
    }

}
