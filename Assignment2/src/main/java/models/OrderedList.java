package models;

import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.ToDoubleFunction;

public interface OrderedList<E> extends List<E> {
    void sort();
    int indexOfByBinarySearch(E searchItem);
    boolean merge(E item, BinaryOperator<E> merger);
    Comparator<? super E> getOrdening();

    default double aggregate(ToDoubleFunction<E> mapper) {
        double sum = 0;
        for (E item : this) {
            // for each item in the list, the mapper will be used to add a value to the sum
            sum += mapper.applyAsDouble(item);
        }
        return sum;
    }
}
