package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the capabilities of the OrderedArrayList class
 *
 * @author jipderksen
 */
public class OrderedArrayListTest {
    OrderedList<Product> products;
    Product product1, product2, product3, product4, product5, product6;

    @BeforeEach
    private void setup() {
        products = new OrderedArrayList<Product>(Comparator.comparing(Product::getBarcode));
        PurchaseTracker.importItemsFromFile(products,
                ProductsListTest.class.getResource("/products12.txt").getPath(),
                Product::fromLine);
        product1 = products.get(0);
        product2 = products.get(1);
        product6 = products.get(5);
        product3 = new Product(101010101010101L, "", 0.0);
        product4 = new Product(202020202020202L, "pindakaas", 2.25);
        product5 = new Product(303030303030303L, "paprika", 0.85);
    }

    @Test
    public void TestBinarySearch() {
        // when there is no sorted part, the index of should return -1
        assertEquals(-1, products.indexOfByBinarySearch(product1));
        // sort is necessary for Binary Search
        products.sort();
        // after the sort, product1 should be in the second position
        assertEquals(1, products.indexOfByBinarySearch(product1));
    }
}
