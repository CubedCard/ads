package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BinaryOperator;

public class OrderedArrayList<E>
        extends ArrayList<E>
        implements OrderedList<E> {

    protected Comparator<? super E> ordening;   // the comparator that has been used with the latest sort
    protected int nSorted;                      // the number of items that have been ordered by barcode in the list
    // representation-invariant
    //      all items at index positions 0 <= index < nSorted have been ordered by the given ordening comparator
    //      other items at index position nSorted <= index < size() can be in any order amongst themselves
    //              and also relative to the sorted section

    public OrderedArrayList() {
        this(null);
    }

    public OrderedArrayList(Comparator<? super E> ordening) {
        super();
        this.ordening = ordening;
        this.nSorted = 0;
    }

    public Comparator<? super E> getOrdening() {
        return this.ordening;
    }

    @Override
    public void clear() {
        super.clear();
        this.nSorted = 0;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.sort(c);
        this.ordening = c;
        this.nSorted = this.size();
    }

    @Override
    public void add(int index, E element) {
        if (index >= nSorted && index < size()) {
            super.add(index, element);
        } else if (index >= 0 && index < nSorted) {
            super.add(size() - 1, element);
        }
    }

    @Override
    public E remove(int index) {
        if (index < nSorted && index >= 0) nSorted--;
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < nSorted && index >= 0) nSorted--;
        return super.remove(o);
    }

    @Override
    public void sort() {
        if (this.nSorted < this.size()) {
            this.sort(this.ordening);
        }
    }

    @Override
    public int indexOf(Object item) {
        if (item != null) {
            return indexOfByBinarySearch((E) item);
        } else {
            return -1;
        }
    }

    @Override
    public int indexOfByBinarySearch(E searchItem) {
        if (searchItem != null) {
            // some arbitrary choice to use the iterative or the recursive version
            return indexOfByIterativeBinarySearch(searchItem);
        } else {
            return -1;
        }
    }

    /**
     * finds the position of the searchItem by an iterative binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByIterativeBinarySearch(E searchItem) {
        if (size() == 0) {
            return -1;
        }
        if (nSorted == 0) {
            return linearSearch(searchItem, nSorted);
        }

        int left = 0, right = nSorted;

        while (left < right) {
            int mid = (left + right) / 2;

            // Check if searchItem is present at mid
            if (ordening.compare(get(mid), searchItem) == 0)
                return mid;

            // If searchItem greater, ignore left half
            if (ordening.compare(get(mid), searchItem) < 0)
                left = mid + 1;

            // If searchItem is smaller, ignore right half
            else
                right = mid;
        }

        // if no match was found, attempt a linear search of searchItem in the section nSorted <= index < size()
        return linearSearch(searchItem, nSorted - 1);
    }

    private int linearSearch(E searchItem, int start) {
        if (start < 0) {
            start = 0;
        }
        for (int i = start; i < size(); i++) {
            if (ordening.compare(searchItem, get(i)) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * finds the position of the searchItem by a recursive binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByRecursiveBinarySearch(E searchItem) {
        int index = recursiveBinarySearch(searchItem, 0, nSorted - 1);
        if (index == -1) {
            index = linearSearch(searchItem, nSorted);
        }
        return index;
    }

    private int recursiveBinarySearch(E searchItem, int from, int to) {
        if (nSorted > 0 && size() > 0) {
            int mid = (from + to) / 2;

            // if the searchItem is exactly in the middle of from and to
            if (ordening.compare(get(mid), searchItem) == 0)
                return mid;

            // if from is greater than to, then the searchItem was not found
            if (from > to)
                return -1;

            // if searchItem is smaller than mid, then it can only be present in left half
            if (ordening.compare(get(mid), searchItem) > 0)
                return recursiveBinarySearch(searchItem, from, mid - 1);

            // else searchItem is greater than mid, so it can only be present in right half
            else
                return recursiveBinarySearch(searchItem, mid + 1, to);
        }

        // if no match was found, attempt a linear search of searchItem in the section nSorted <= index < size()
        return -1;
    }

    /**
     * finds a match of newItem in the list and applies the merger operator with the newItem to that match
     * i.e. the found match is replaced by the outcome of the merge between the match and the newItem
     * If no match is found in the list, the newItem is added to the list.
     *
     * @param newItem
     * @param merger  a function that takes two items and returns an item that contains the merged content of
     *                the two items according to some merging rule.
     *                e.g. a merger could add the value of attribute X of the second item
     *                to attribute X of the first item and then return the first item
     * @return whether a new item was added to the list or not
     */
    @Override
    public boolean merge(E newItem, BinaryOperator<E> merger) {
        if (newItem == null) return false;
        int matchedItemIndex = this.indexOfByRecursiveBinarySearch(newItem);

        if (matchedItemIndex < 0) {
            this.add(newItem);
            return true;
        } else {
            E matchedItem = get(matchedItemIndex);
            set(matchedItemIndex, merger.apply(matchedItem, newItem));
            return false;
        }
    }
}
