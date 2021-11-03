package nl.hva.ict.ads;

import java.util.Comparator;
import java.util.List;

public class SorterImpl<E> implements Sorter<E> {

    /**
     * Sorts all items by selection or insertion sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items      is the list of items that needs to be sorted using selection -or insertion sort
     * @param comparator this is used to compare the items in the items list with each other
     * @return the items sorted in place
     */
    public List<E> selInsSort(List<E> items, Comparator<E> comparator) {
        for (int i = 1; i < items.size(); ++i) {
            E insertItem = items.get(i); // the first unsorted item in the list
            int j = i - 1;
            // loop back through the array till the found item isn't smaller than the item to be inserted
            while (j >= 0 && (comparator.compare(items.get(j), insertItem) > 0)) {
                // move the smaller item one position further in the array
                items.set(j + 1, items.get(j));
                j--;
            }
            /*
            set the new position of the insert item to this position when the position of j is out of bounds and /
            or the found item isn't smaller than the insertItem
            */
            items.set(j + 1, insertItem);
        }
        return items;
    }

    /**
     * Sorts all items by quick sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items      is the list of items that needs to be sorted using quick sort
     * @param comparator this is used to compare the items in the items list with each other
     * @return the items sorted in place
     */
    public List<E> quickSort(List<E> items, Comparator<E> comparator) {
        // sort the complete list of items from position 0 till size-1, encluding position size
        this.quickSortPart(items, 0, items.size() - 1, comparator);
        return items;
    }

    /**
     * Sorts all items between index positions 'from' and 'to' inclusive by quick sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array or other positions in items
     *
     * @param items      is the list of items that needs to be sorted
     * @param comparator this is used to compare the items in the items list with each other
     * @return the items sorted in place (then why is this a void method?)
     */
    private void quickSortPart(List<E> items, int from, int to, Comparator<E> comparator) {
        if (from < to) {
            // get the index of the pivot
            int pivot = partition(items, from, to, comparator);
            // sort left half from pivot
            quickSortPart(items, from, pivot - 1, comparator);
            // sort right half from pivot
            quickSortPart(items, pivot + 1, to, comparator);
        }
    }

    /**
     * This method finds the pivot item and then pivots the items[from...to] around it.
     * Meaning larger and smaller on their own side.
     *
     * @param items      the list of items to be (partly) sorted
     * @param from       from which index to sort the list
     * @param to         to which index to sort the list
     * @param comparator the function that is used to compare items with each other
     * @return the (new) index of the pivot item
     */
    private int partition(List<E> items, int from, int to, Comparator<E> comparator) {
        int low = from + 1;
        int high = to;
        E pivot = items.get(from);

        do {
            // ignore the items that don't need to be pivoted
            while (low <= high && (comparator.compare(items.get(low), pivot) < 0)) low++;
            while (low <= high && (comparator.compare(items.get(high), pivot) > 0)) high--;
            if (low <= high) {
                // switch the items
                items.set(low, items.set(high, items.get(low)));
                low++;
                high--;
            }
        } while (low <= high);

        // put the pivot item in its place
        items.set(from, items.set(high, pivot));
        return high;
    }

    /**
     * Identifies the lead collection of numTops items according to the ordening criteria of comparator
     * and organizes and sorts this lead collection into the first numTops positions of the list
     * with use of (zero-based) heapSwim and heapSink operations.
     * The remaining items are kept in the tail of the list, in arbitrary order.
     * Items are sorted 'in place' without use of an auxiliary list or array or other positions in items
     *
     * @param numTops    the size of the lead collection of items to be found and sorted
     * @param items      is the list of items that needs to be sorted using heap sort
     * @param comparator this is used to compare the items in the items list with each other
     * @return the items list with its first numTops items sorted according to comparator
     * all other items >= any item in the lead collection
     */
    public List<E> topsHeapSort(int numTops, List<E> items, Comparator<E> comparator) {
        // check 0 < numTops <= items.size()
        if (numTops <= 0) return items;
        else if (numTops > items.size()) return quickSort(items, comparator);

        // the lead collection of numTops items will be organised into a (zero-based) heap structure
        // in the first numTops list positions using the reverseComparator for the heap condition.
        // that way the root of the heap will contain the worst item of the lead collection
        // which can be compared easily against other candidates from the remainder of the list
        Comparator<E> reverseComparator = comparator.reversed();

        // initialise the lead collection with the first numTops items in the list
        for (int heapSize = 2; heapSize <= numTops; heapSize++) {
            // repair the heap condition of items[0..heapSize-2] to include new item items[heapSize-1]
            heapSwim(items, heapSize, reverseComparator);
        }

        // insert remaining items into the lead collection as appropriate
        for (int i = numTops; i < items.size(); i++) {
            // loop-invariant: items[0..numTops-1] represents the current lead collection in a heap data structure
            //  the root of the heap is the currently trailing item in the lead collection,
            //  which will lose its membership if a better item is found from position i onwards
            E item = items.get(i);
            E worstLeadItem = items.get(0);
            if (comparator.compare(item, worstLeadItem) < 0) {
                // item < worstLeadItem, so shall be included in the lead collection
                items.set(0, item);
                // demote worstLeadItem back to the tail collection, at the orginal position of item
                items.set(i, worstLeadItem);
                // repair the heap condition of the lead collection
                heapSink(items, numTops, reverseComparator);
            }
        }

        // the first numTops positions of the list now contain the lead collection
        // the reverseComparator heap condition applies to this lead collection
        // now use heapSort to realise full ordening of this collection
        for (int i = numTops - 1; i > 0; i--) {
            // loop-invariant: items[i+1..numTops-1] contains the tail part of the sorted lead collection
            // position 0 holds the root item of a heap of size i+1 organised by reverseComparator
            // this root item is the worst item of the remaining front part of the lead collection

            items.set(0, items.set(i, items.get(0)));

            // TODO the new root may have violated the heap condition
            //  repair the heap condition on the remaining heap of size i
            heapSwim(items, i, comparator);

        }
        // alternatively we can realise full ordening with a partial quicksort:
        // quickSortPart(items, 0, numTops-1, comparator);

        return items;
    }

    /**
     * Repairs the zero-based heap condition for items[heapSize-1] on the basis of the comparator
     * all items[0..heapSize-2] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     * all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     *
     * @param items      is the list of items that needs to be sorted
     * @param comparator this is used to compare the items in the items list with each other
     * @param heapSize
     */
    private void heapSwim(List<E> items, int heapSize, Comparator<E> comparator) {
        // TODO swim items[heapSize-1] up the heap until
        //      i==0 || items[(i-1]/2] <= items[i]
        int i = heapSize;
        while (!(i == 0 || comparator.compare(items.get((i - 1) / 2), items.get(i)) <= 0)) {
            items.set((i - 1) / 2, items.set(i, items.get((i - 1) / 2)));
            i = (i - 1) / 2;
        }
    }

    /**
     * Repairs the zero-based heap condition for its root items[0] on the basis of the comparator
     * all items[1..heapSize-1] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     * all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     *
     * @param items      is the array that needs to be sorted
     * @param comparator this is used to compare the items in the items list with each other
     * @param heapSize
     */
    private void heapSink(List<E> items, int heapSize, Comparator<E> comparator) {
        // TODO sink items[0] down the heap until
        //      2*i+1>=heapSize || (items[i] <= items[2*i+1] && items[i] <= items[2*i+2])
        int i = 0;
        while (!(2 * i + 1 >= heapSize || (comparator.compare(items.get(i), items.get(2 * i + 1)) <= 0
                && comparator.compare(items.get(i), items.get(2 * i + 2)) <= 0))) {
            if (comparator.compare(items.get(2 * i + 1), items.get(2 * i + 2)) > 0) {
                items.set(2 * i + 1, items.set(i, items.get(2 * i + 1)));
                i = 2 * i + 1;
            } else {
                items.set(2 * i + 2, items.set(i, items.get(2 * i + 2)));
                i = 2 * i + 2;
            }
        }
    }
}
