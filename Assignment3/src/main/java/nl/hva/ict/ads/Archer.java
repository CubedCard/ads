package nl.hva.ict.ads;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Archer {
    public static int MAX_ARROWS = 3;
    public static int MAX_ROUNDS = 10;

    private static int identifier = 135788;
    private final int id;
    private final String firstName;
    private final String lastName;
    private final Map<Integer, int[]> scores;

    /**
     * Constructs a new instance of Archer and assigns a unique id to the instance.
     * Each new instance should be assigned a number that is 1 higher than the last one assigned.
     * The first instance created should have ID 135788;
     *
     * @param firstName the archers first name.
     * @param lastName  the archers surname.
     */
    public Archer(String firstName, String lastName) {
        this.id = Archer.identifier++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.scores = new HashMap<>();

        int[] scoresArrows = new int[Archer.MAX_ARROWS];

//        for (int i = 0; i < Archer.MAX_ROUNDS; i++) {
//            for (int j = 0; j < Archer.MAX_ARROWS; j++) scoresArrows[j] = (int) Math.floor(Math.random() * 11);
//            this.registerScoreForRound(i + 1, scoresArrows);
//            scoresArrows = new int[Archer.MAX_ARROWS];
//        }
    }

    /**
     * Registers the points for each of the three arrows that have been shot during a round.
     *
     * @param round  the round for which to register the points. First round has number 1.
     * @param points the points shot during the round, one for each arrow.
     */
    public void registerScoreForRound(int round, int[] points) {
        scores.put(round, Arrays.copyOf(points, points.length));
    }


    /**
     * Calculates/retrieves the total score of all arrows across all rounds
     *
     * @return the total scored points
     */
    public int getTotalScore() {
        int totalScore = 0;
        for (int round = 1; round <= Archer.MAX_ROUNDS; round++) {
            if (scores.get(round) != null) {
                for (int score : scores.get(round)) {
                    totalScore += score;
                }
            }
        }
        return totalScore;
    }

    /**
     * compares the scores/id of this archer with the scores/id of the other archer according to
     * the scoring scheme: highest total points -> least misses -> earliest registration
     * The archer with the lowest id has registered first
     *
     * @param other the other archer to compare against
     * @return negative number, zero or positive number according to Comparator convention
     */
    public int compareByHighestTotalScoreWithLeastMissesAndLowestId(Archer other) {
        if (this.getTotalScore() != other.getTotalScore()) return other.getTotalScore() - this.getTotalScore();

        int timesScoredZeroCurrent = 0;
        int timesScoredZeroOther = 0;

        for (int i = 1; i <= MAX_ROUNDS; i++) {
            for (int score : this.getScores().get(i)) if (score == 0) timesScoredZeroCurrent++;
            for (int score : other.getScores().get(i)) if (score == 0) timesScoredZeroOther++;
        }

        if (timesScoredZeroCurrent == timesScoredZeroOther) return this.getId() - other.getId();

        return timesScoredZeroCurrent - timesScoredZeroOther;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Map<Integer, int[]> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return String.format("%d (%d) %s %s",
                getId(), getTotalScore(), getFirstName(), getLastName()
        );
    }
}
