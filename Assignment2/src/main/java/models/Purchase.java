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
        String[] splitText = textLine.split(", ");

        Product product1;
        int count = 0;
        // get the index of the product in the array with the same barcode as the given barcode
        int index = products.indexOf(new Product(Long.parseLong(splitText[0])));

        if (index < 0)
            return null; // product not found

        product1 = products.get(index);
        // if the textLine contains another item, it will be assigned to the count
        if (splitText.length > 1) count = Integer.parseInt(splitText[1]);

        return new Purchase(product1, count);
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
}
