import models.Purchase;
import models.PurchaseTracker;

import java.util.Comparator;

public class SupermarketStatisticsMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Supermarket Statistics processor\n");

        PurchaseTracker purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");

        purchaseTracker.importPurchasesFromVault("/purchases");

        // TODO provide the comparators that can order the purchases by specified criteria
        Comparator<Purchase> compareByRevenue =
                (o1, o2) -> (int) (o1.getCount() * o1.getProduct().getPrice() - (o2.getCount() * o2.getProduct().getPrice()));

        Comparator<Purchase> compareByValue =
                (o1, o2) -> {
                    o1.addCount(o2.getCount());
                    return o1.getCount();
                };

        purchaseTracker.showTops(5, "worst sales volume",
                compareByValue
        );
        purchaseTracker.showTops(5, "best sales revenue",
                compareByRevenue
        );

        purchaseTracker.showTotals();
    }


}
