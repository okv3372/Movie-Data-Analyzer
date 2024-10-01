package movies;

import java.util.Objects;

/**
 * Represents a movie rating.  Movie's naturally order by descending rating
 * and then descending number of votes and then unique ID.
 *
 * @author RIT CS
 * @author Oliver Varney
 */
public final class Rating implements Comparable<Rating> {
    /** the unique string ID of the movie */
    private final String ID;
    /** it's rating between 0.0-10.0 */
    private final double rating;
    /** the total number of ratings received */
    private final int numVotes;

    /**
     * Create the new instance.
     *
     * @param ID the unique movie ID (tconst)
     * @param rating the rating
     * @param numVotes the total number of ratings
     */
    public Rating(String ID, double rating, int numVotes) {
        this.ID = ID;
        this.rating = rating;
        this.numVotes = numVotes;
    }

    /**
     * Get the movie ID for this rating.
     *
     * @return the ID (tconst)
     */
    public String getID() {
        return this.ID;
    }

    /**
     * Get the movie rating number.
     *
     * @return the rating
     */
    public double getRating() {
        return this.rating;
    }

    /**
     * Get the number of ratings this movie received.
     *
     * @return the number of votes
     */
    public int getNumVotes() {
        return this.numVotes;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ID='" + this.ID + '\'' +
                ", rating=" + this.rating +
                ", numVotes=" + this.numVotes +
                '}';
    }

    /**
     * Two ratings are equal if the have the same rating, vote
     * @param other the object to compare to
     * @return whether or not they are equal
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Rating) {
            Rating otherRating = (Rating) other;
            result = this.rating == otherRating.rating &&
                    this.numVotes == otherRating.numVotes &&
                    this.ID.equals(otherRating.ID);
        }
        return result;
    }

    /**
     * The hash code is the Object.hash() of each of rating, numVotes and ID fields
     * @return the hash code of this Rating
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.rating, this.numVotes, this.ID);
    }

    /**
     * The natural order comparison of Rating's is by descending rating,
     * then descending number of votes, then ascending by ID.
     *
     * @return whether this rating is less than, equal, or greater than the
     * other rating.
     */
    @Override
    public int compareTo(Rating other) {
        int ratingDif = Double.compare(this.rating, other.rating);
        int voteNumber = Double.compare(this.numVotes, other.numVotes);

        if((-ratingDif) == 0){
            if((-voteNumber) == 0){
                return this.ID.compareTo(other.ID);
            }
            return -(voteNumber);
        }
        return (-ratingDif);  // remove when implemented correctly
        // to compare two doubles, d1, d2, with an integer result, use: Double.compare(d1, d2)
    }
}
