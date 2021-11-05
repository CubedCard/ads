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
    }

    /**
     * Registers the points for each of the three arrows that have been shot during a round.
     *
     * @param round  the round for which to register the points. First round has number 1.
     * @param points the points shot during the round, one for each arrow.
     */
    public void registerScoreForRound(int round, int[] points) {
        // check if the correct number of scores where given
        if (points.length == Archer.MAX_ARROWS) scores.put(round, Arrays.copyOf(points, points.length));
    }


    /**
     * Calculates/retrieves the total score of all arrows across all rounds
     *
     * @return the total scored points
     */
    public int getTotalScore() {
        int totalScore = 0; // will hold the total score of the rounds
        // iterate through the rounds and then the scores to add them to the totalScore variable
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
        // put the total scores in a variable be more time efficient (not calling the method a second time)
        int totalScoreCurrent = this.getTotalScore();
        int totalScoreOther = other.getTotalScore();

        // first check if the total scores are not equal, because then you can compare the total scores
        if (totalScoreCurrent != totalScoreOther) return totalScoreOther - totalScoreCurrent;

        int timesScoredZeroCurrent = 0; // keeps track of the number of misses of the current archer
        int timesScoredZeroOther = 0; // keeps track of the number of misses of the other archer

        // iterate through the scores and increment the corresponding variable when a zero was scored by the archer
        for (int i = 1; i <= MAX_ROUNDS; i++) {
            for (int score : this.getScores().get(i)) if (score == 0) timesScoredZeroCurrent++;
            for (int score : other.getScores().get(i)) if (score == 0) timesScoredZeroOther++;
        }

        // if the number of misses are not equal, compare these
        if (timesScoredZeroCurrent != timesScoredZeroOther) return timesScoredZeroCurrent - timesScoredZeroOther;

        // finally, if the number of misses is equal, compare by registration (id)
        return this.getId() - other.getId();
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
