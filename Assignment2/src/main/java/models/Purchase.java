package models;

import java.util.List;

public class Purchase {
    private final Product product;
    private int count;

    public Purchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    /**
     * parses purchase summary information from a textLine with format: barcode, amount
     *
     * @param textLine
     * @param products a list of products ordered and searchable by barcode
     *                 (i.e. the comparator of the ordered list shall consider only the barcode when comparing products)
     * @return a new Purchase instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Purchase fromLine(String textLine, List<Product> products) {
        Purchase newPurchase = null;
        String[] splitText = textLine.split(", ");

        Product product1 = null;
        int count = 0;

        if (splitText.length > 0) product1 = products.get(products.indexOf(new Product(Long.parseLong(splitText[0]))));
        if (splitText.length > 1) count = Integer.parseInt(splitText[1]);

        if (product1 != null) newPurchase = new Purchase(product1, count);

        return newPurchase;
    }

    /**
     * add a delta amount to the count of the purchase summary instance
     *
     * @param delta
     */
    public void addCount(int delta) {
        this.count += delta;
    }

    public long getBarcode() {
        return this.product.getBarcode();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return String.format("%d/%s/%d/%.2f", product.getBarcode(), product.getTitle(), count,
                (product.getPrice() * count));
    }

    // TODO add public and private methods as per your requirements

    public static Purchase fromLineWithoutProducts(String textLine) {
        Purchase newPurchase;
        String[] splitText = textLine.split(", ");

        Product product1;
        int count;

        if (splitText.length < 2) {
            return null;
        }

        if (splitText[0].isEmpty() || splitText[1].isEmpty()) return null;

        product1 = new Product(Long.parseLong(splitText[0]));
        count = Integer.parseInt(splitText[1]);

        newPurchase = new Purchase(product1, count);

        return newPurchase;
    }
}
