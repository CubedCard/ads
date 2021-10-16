import models.Purchase;
import models.PurchaseTracker;

import java.util.Comparator;

public class SupermarketStatisticsMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Supermarket Statistics processor\n");

        PurchaseTracker purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");

        purchaseTracker.importPurchasesFromVault("/purchases");

        Comparator<Purchase> compareByValue =
                Comparator.comparingInt(Purchase::getCount);

        Comparator<Purchase> compareByRevenue =
                (o1, o2) -> (int) ((int) (o1.getCount() * o1.getProduct().getPrice()) - (o2.getCount() * o2.getProduct().getPrice()));

        purchaseTracker.showTops(5, "worst sales volume",
                compareByValue
        );
        purchaseTracker.showTops(5, "best sales revenue",
                compareByRevenue.reversed()
        );

        purchaseTracker.showTotals();
    }


}
