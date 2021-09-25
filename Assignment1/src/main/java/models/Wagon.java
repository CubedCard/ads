package models;

public abstract class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon(int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    /**
     * @return whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        return nextWagon != null;
    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        return previousWagon != null;
    }

    /**
     * Returns the last wagon attached to it, if there are no wagons attached to it then this wagon is the last wagon.
     *
     * @return the wagon
     */
    public Wagon getLastWagonAttached() {
        if (hasNextWagon()) return nextWagon.getLastWagonAttached();
        return this;
    }

    /**
     * @return the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {
        if (hasNextWagon()) return nextWagon.getTailLength() + 1;
        return 0;
    }

    /**
     * Attaches the tail wagon behind this wagon, if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     *
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {
        if (this.hasNextWagon()) throw new IllegalStateException(String.format(
                "attach has failed because %s has a next wagon: %s", this, this.nextWagon));
        if (tail.hasPreviousWagon()) throw new IllegalStateException(String.format(
                "attach has failed because %s has a previous wagon: %s", tail, tail.previousWagon));
        tail.previousWagon = this;
        nextWagon = tail;
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     *
     * @return the first wagon of the tail that has been detached
     * or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        // I chose to use 'try-finally' because I find it easier to read then when using a temporary Wagon
        try {
            return nextWagon;
        } finally {
            if (hasNextWagon()) nextWagon.previousWagon = null;
            nextWagon = null;
        }
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     *
     * @return the former previousWagon that has been detached from,
     * or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        try {
            return previousWagon;
        } finally {
            if (hasPreviousWagon()) previousWagon.nextWagon = null;
            previousWagon = null;
        }
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     *
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        if (front != null) {
            if (front.hasNextWagon()) front.nextWagon.previousWagon = null;
            front.nextWagon = this;
        }
        if (hasPreviousWagon()) previousWagon.nextWagon = null;
        previousWagon = front;
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        if (this.hasPreviousWagon()) previousWagon.nextWagon = nextWagon;
        if (this.hasNextWagon()) nextWagon.previousWagon = previousWagon;
        nextWagon = null;
        previousWagon = null;
    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     *
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        Wagon start;
        if (hasPreviousWagon()) {
            Wagon prev = previousWagon;
            prev.detachTail();
            start = reverseSequenceFrom(this);
            start.reAttachTo(prev);
        } else start = reverseSequenceFrom(this);
        return start;
    }

    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     *
     * @param wagon the 'head' of the sequence that will be reversed
     * @return the new 'head' of the sequence
     */
    private Wagon reverseSequenceFrom(Wagon wagon) {
        Wagon wagon1 = wagon;
        Wagon wagon2 = wagon.nextWagon;

        wagon1.nextWagon = null;
        wagon1.previousWagon = wagon2;

        while (wagon2 != null) {
            wagon2.previousWagon = wagon2.nextWagon;
            wagon2.nextWagon = wagon1;
            wagon1 = wagon2;
            wagon2 = wagon2.previousWagon;
        }
        return wagon1;
    }

    @Override
    public String toString() {
        return String.format("[Wagon-%d]", id);
    }
}
