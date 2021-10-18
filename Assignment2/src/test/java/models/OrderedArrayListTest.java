package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the capabilities of the OrderedArrayList class
 *
 * @author jipderksen & Yaël de Vries
 */
public class OrderedArrayListTest {
    OrderedList<Product> products;
    Product product1, product2, product3, product4, product5, product6, product7, product8;

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
        // since the OrderedArrayList is not sorted, product1 should be at position 0 (see BeforeEach)
        assertEquals(0, products.indexOf(product1));
        // sort is necessary for Binary Search
        products.sort();
        // after the sort, product1 should be in the second position
        assertEquals(1, products.indexOf(product1));
    }

    // To test if object is null and returns.
    @Test
    public void TestObjectsExistence() {
        // Assign null to element in question.
        product7 = null;

        // Get the index of product, which does not exist.
        assertEquals(-1, products.indexOf(product7));

        // Initiate new product on same variable but not added to list.
        product7 = new Product(404040404040404L, "Kaas met pepernoten", 2.5);

        // Get the index of element not added to the list.
        assertEquals(-1, products.indexOf(product7));

        // Element added to list.
        products.add(product7);

        // Check if element passes the check in indexOf().
        assertEquals(12, products.indexOf(product7));
    }

    // The next test makes sure that the element gets placed in the index of choice when adding it to the unsorted part of the list.
    @Test
    public void TestUnsortedIndexOfAddedElement() {
        // Create a nww element.
        product8 = new Product(505050505050505L, "Grijze handschoenen met bizonhaar", 25.95);

        // Making sure the element gets added to the unsorted list.
        products.add(11, product8);

        // Check if the element gets added to the right index.
        assertEquals(11, products.indexOf(product8));
    }
}
