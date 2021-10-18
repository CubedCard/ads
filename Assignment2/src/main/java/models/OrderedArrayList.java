package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BinaryOperator;

public class OrderedArrayList<E>
        extends ArrayList<E>
        implements OrderedList<E> {

    protected Comparator<? super E> ordening;   // the comparator that has been used with the latest sort
    private int nSorted;                        // the number of items that have been ordered by barcode in the list
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
        // when the index is from nSorted to size, it will be added at the given index
        if (index >= nSorted && index < size()) {
            super.add(index, element);
        }
        // when the index is inside the sorted section, the element will be added at the end
        else if (index >= 0 && index < nSorted) {
            super.add(size() - 1, element);
        }
    }

    @Override
    public E remove(int index) {
        // when the index is inside the sorted section of the array, the variable nSorted needs to be adjusted
        if (index < nSorted && index >= 0) nSorted--;
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        // when the index is inside the sorted section of the array, the variable nSorted needs to be adjusted
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
            // when looking at the time stamps of the tests, the times didn't vary that much
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
    private int indexOfByIterativeBinarySearch(E searchItem) {
        if (size() == 0) {
            return -1; // there are no items to search for
        }
        if (nSorted == 0) {
            return linearSearch(searchItem, nSorted); // there is no sorted section, so a linear search is necessary
        }

        int left = 0, right = nSorted;

        while (left < right) {
            int mid = (left + right) / 2;

            // Check if searchItem is present at the middle
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

    /**
     * This method will try to find a given item using linear search
     *
     * @param searchItem the item that needs to be found
     * @param start      the starting index in the list
     * @return the index that was found or -1 if no index was found
     */
    private int linearSearch(E searchItem, int start) {
        if (start < 0) {
            start = 0; // a negative index will return an IndexOutOfBoundsException
        }
        for (int i = start; i < size(); i++) {
            if (ordening.compare(searchItem, get(i)) == 0) {
                return i; // when the searchItem is equal to the item at the index, the index will be returned
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
    private int indexOfByRecursiveBinarySearch(E searchItem) {
        int index = recursiveBinarySearch(searchItem, 0, nSorted - 1);
        if (index == -1) {
            index = linearSearch(searchItem, nSorted - 1);
        }
        return index;
    }

    /**
     * This method tries to find an item recursively
     *
     * @param searchItem the item to be found
     * @param from       the index from which the method needs to search
     * @param to         the index to which the method needs to search
     * @return the found index of searchItem or -1 if no index was found
     */
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
     * @param newItem the item that will be added or merged
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

        // when no index was found, the item is not yet in the OrderedArrayList and will be added
        if (matchedItemIndex < 0) {
            this.add(newItem);
            return true;
        } else {
            // when an index was found, an attribute of the array will be merged with the newItem
            E matchedItem = get(matchedItemIndex);
            set(matchedItemIndex, merger.apply(matchedItem, newItem)); // set the found item to the merged item
            return false;
        }
    }
}
