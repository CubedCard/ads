package models;

public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /* three helper methods that are usefull in other methods */
    public boolean hasWagons() {
        // TODO
        return firstWagon != null;
    }

    public boolean isPassengerTrain() {
        // TODO
        if (hasWagons()) return firstWagon instanceof PassengerWagon;
        return false;
    }

    public boolean isFreightTrain() {
        // TODO
        if (hasWagons()) return firstWagon instanceof FreightWagon;
        return false;
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     */
    public void setFirstWagon(Wagon wagon) {
        // TODO
        if (wagon != null) firstWagon = wagon;
    }

    /**
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        // TODO
        int numberOfWagons = 0;
        Wagon next = firstWagon;
        while (next != null) {
            numberOfWagons++;
            next = next.getNextWagon();
        }
        return numberOfWagons;
    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        // TODO
        if (hasWagons()) return firstWagon.getLastWagonAttached();
        return firstWagon;
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        // TODO
        if (isPassengerTrain()) {
            int totalNumberOfSeats = 0;
            Wagon next = firstWagon;
            while (next != null) {
                totalNumberOfSeats += ((PassengerWagon) next).getNumberOfSeats();
                next = next.getNextWagon();
            }
            return totalNumberOfSeats;
        }
        return 0;
    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        // TODO
        if (isFreightTrain()) {
            int totalMaxWeight = 0;
            Wagon next = firstWagon;
            while (next != null) {
                totalMaxWeight += ((FreightWagon) next).getMaxWeight();
                next = next.getNextWagon();
            }
            return totalMaxWeight;
        }
        return 0;
    }

    /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     *
     * @param position
     * @return the wagon found at the given position
     * (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        // TODO
        if (hasWagons() && position > 0 && position <= getNumberOfWagons()) {
            Wagon next = firstWagon;
            for (int i = 1; i < position; i++) next = next.getNextWagon();
            return next;
        }
        return null;
    }

    /**
     * Finds the wagon with a given wagonId
     *
     * @param wagonId
     * @return the wagon found
     * (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        // TODO
        if (hasWagons()) {
            Wagon current = firstWagon;
            while (current != null) {
                if (wagonId == current.getId()) return current;
                current = current.getNextWagon();
            }
        }
        return null;
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return true if the sequence can be added and false if the sequence can't be added
     */
    public boolean canAttach(Wagon wagon) {
        // TODO
        if (wagon != null && engine.getMaxWagons() > (getNumberOfWagons() + wagon.getTailLength()) && findWagonById(wagon.getId()) == null) {
            if (hasWagons()) {
                if (wagon instanceof FreightWagon) return isFreightTrain();
                if (wagon instanceof PassengerWagon) return isPassengerTrain();
            } else return true;
        }
        return false;
    }

    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {
        // TODO
        if (canAttach(wagon)) {
            if (hasWagons()) wagon.reAttachTo(getLastWagonAttached());
            else firstWagon = wagon;
            return true;
        }
        return false;
    }


    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        // TODO
        if (!canAttach(wagon)) return false;
        if (hasWagons()) wagon.getLastWagonAttached().attachTail(firstWagon);
        wagon.detachFront();
        firstWagon = wagon;
        return true;
    }

    /**
     * Tries to insert the given sequence of wagons at/before the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible of the engine has insufficient capacity
     * or the given position is not valid in this train)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon wagon) {
        // TODO
        if (position == 1) return insertAtFront(wagon);
        Wagon current = findWagonAtPosition(position);
        if (current == null || !canAttach(wagon)) {
            return false;
        }
        wagon.reAttachTo(current.detachFront());
        current.reAttachTo(wagon.getLastWagonAttached());
        return true;
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param wagonId
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        Wagon wagonToMove = findWagonById(wagonId);
        if (toTrain.canAttach(wagonToMove)) {
            if (firstWagon == wagonToMove) firstWagon = wagonToMove.detachTail();
            else {
                if (wagonToMove.hasNextWagon()) wagonToMove.detachTail().reAttachTo(wagonToMove.detachFront());
                else wagonToMove.detachFront();
            }
            toTrain.attachToRear(wagonToMove);
            return true;
        }
        return false;
    }

    /**
     * Tries to split this train before the given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param position
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {
        // TODO
        Wagon wagonAtPosition = findWagonAtPosition(position);
        if (toTrain.canAttach(wagonAtPosition)) {
            if (wagonAtPosition == firstWagon) firstWagon = null;
            else wagonAtPosition.detachFront();
            toTrain.attachToRear(wagonAtPosition);
            return true;
        }
        return false;
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        // TODO
        if (hasWagons() && firstWagon.hasNextWagon()) firstWagon = firstWagon.reverseSequence();
    }

    // TODO

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Wagon next = firstWagon;
        while (next != null) {
            stringBuilder.append(next);
            next = next.getNextWagon();
        }
        return String.format("%s%s with %d wagons from %s to %s", engine, stringBuilder,
                getNumberOfWagons(), origin, destination);
    }

    // removed the overloaded methods at the bottom of the Train class
}
