package models;
// TODO
public class FreightWagon extends Wagon {
    private final int maxWeight;

    public FreightWagon(int wagonId, int maxWeight) {
        super(wagonId);
        this.maxWeight = maxWeight;
    }

    public int getMaxWeight() {
        // TODO
        return maxWeight;
    }
}
