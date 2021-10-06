package models;

public class Product {
    private final long barcode;
    private String title;
    private double price;

    public Product(long barcode) {
        this.barcode = barcode;
    }

    public Product(long barcode, String title, double price) {
        this(barcode);
        this.title = title;
        this.price = price;
    }

    /**
     * parses product information from a textLine with format: barcode, title, price
     *
     * @param textLine
     * @return a new Product instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Product fromLine(String textLine) {
        Product newProduct = null;
        String[] splitText;

        splitText = textLine.split(", ");

        long barcode = 0L;
        String title = null;
        double price = 0;

        if (splitText.length > 0) barcode = Long.parseLong(splitText[0]);
        if (splitText.length > 1) title = splitText[1];
        if (splitText.length > 2) price = Double.parseDouble(splitText[2]);

        newProduct = new Product(barcode, title, price);

        return newProduct;
    }

    public long getBarcode() {
        return barcode;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product)) return false;
        return this.getBarcode() == ((Product) other).getBarcode();
    }

    // TODO add public and private methods as per your requirements

    @Override
    public String toString() {
        return String.format("%d/%s/%.2f",
                getBarcode(),
                getTitle(),
                getPrice()
        );
    }
}
