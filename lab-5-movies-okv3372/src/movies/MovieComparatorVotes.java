package movies;

import java.util.Comparator;

/**
 * A comparator that orders Movie's by descending number of votes (in Rating)
 * and then alphabetically by title.
 *
 * @author RIT CS
 * @author Oliver Varney
 */
public class MovieComparatorVotes implements Comparator<Movie> {
    /**
     * Compare two Movies by votes.
     *
     * @param m1 the first movie
     * @param m2 the second movie
     * @return less than 0 if m2 has less votes than m1.  if number of votes
     * are equal it is the natural order comparison of m1 to m2's titles.
     * equal if both number of votes and title are the same.  greater than 0
     * if m2's votes is greater than m1's.
     */
    @Override
    public int compare(Movie m1, Movie m2) {
        Rating m1Rating = m1.getRating();
        Rating m2Rating = m2.getRating();
        int ratingDif = m2Rating.getNumVotes() - m1Rating.getNumVotes();
        if(ratingDif == 0){
            return m1.getTitle().compareTo(m2.getTitle());
        } else{
            return ratingDif;
        }

    }
}
