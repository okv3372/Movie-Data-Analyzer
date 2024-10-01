package movies;

import cs.Genre;
import cs.MovieMaps;
import cs.TitleType;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * The subclass of the IMDB abstract class that implements all the required
 * abstract query methods.
 *
 * @author RIT CS
 * @author Oliver Varney
 */
public class MyIMDB extends IMDB {
    /** The minimum number of votes a movie needs to be considered for top ranking */
    private final static int MIN_NUM_VOTES_FOR_TOP_RANKED = 1000;

    /**
     * Create IMDB using the small or large dataset.
     *
     * @param small true if the small dataset is desired, otherwise the large one
     * @throws FileNotFoundException
     */
    public MyIMDB(boolean small) throws FileNotFoundException {
        super(small);
    }

    /**
     *Find all movies of a certain type that contain the words as a substring (case sensitive).
     * This routine should use the protected movieList to perform the search
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param words the words as a string that the movie title must contain to match
     * @return the collection of movies that match (order determined by order in file).
     */
    @Override
    public Collection<Movie> getMovieTitleWithWords(String type, String words) {
        // we simply loop over movieList and add to our list the movies that
        // have the same type, and contain the words substring
        List<Movie> result = new LinkedList<>();

        for(Movie movie: this.movieList) {
            TitleType titleType = TitleType.valueOf(type);   // titleType is TitleType.MOVIE
            // for a particular Movie, movie
            if (movie.getTitleType() == titleType) {
                if(movie.getTitle().contains(words)){
                    result.add(movie);
                }
            }
        }
        return result;
    }

    /**
     *Find a movie by a certain ID (a unique tConst string). This routine has a precondition that IMDB's
     * convertMovieMapToList has already been called, and the map, movieMap, has been created. This routine must use
     * movieMap to perform the lookup as an expected O(1) operation.
     * @param ID the movie's tConst string ID
     *
     * @return the matching Movie object, or null if not found
     */
    @Override
    public Movie findMovieByID(String ID) {
        return this.movieMap.get(ID);
    }

    /**
     * Find movies of a certain type for a specific year that are a certain genre.
     * The movies that are returned should be ordered alphabetically by title.
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param year the year
     * @param genre the genre, e.g. "Crime", "Drama", etc.
     * @return movies ordered alphabetically by title
     */
    @Override
    public Collection<Movie> getMoviesByYearAndGenre(String type, int year, String genre) {
        // we use Movie's natural order comparison which is to order Movie's of a
        // type by title and then year
        Set<Movie> result = new TreeSet<>();
        for(Movie movie: this.movieList){
            Genre genreMatch = Genre.valueOf(genre);
            TitleType titleType = TitleType.valueOf(type);   // titleType is TitleType.MOVIE
            if (movie.getGenres().contains(genreMatch) &&
                    movie.getTitleType() == titleType &&
                        movie.getYear() == year) {
                        result.add(movie);
                    }
                }
            return result;
        }

    /**
     * For a range of inclusive years, count the number of movies released of a certain type grouped by genre. The
     * collection returned should have stored the number movies of the given type per year and genre.
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param start the start year (inclusive)
     * @param end the end year (inclusive)
     * @return the map is keyed by year from start to end inclusive, and the values for each year is another map keyed
     * by genre ordered alphabetically. The values of the genre maps is the number of movies in that genre.
     * */
    @Override
    public Map<Integer, Map<Genre, Integer>> getCountOfMoviesByGenre(String type, int start, int end){
        Map<Integer, Map<Genre, Integer>> result = new TreeMap<>();
        TitleType titleType = TitleType.valueOf(type);   // titleType is TitleType.MOVIE

        for(int i = start; i < end + 1; i++){
            result.put(i, new TreeMap<>());
        }
        for(Movie movie: this.movieList){
            int movieYear = movie.getYear();
            if(movie.getTitleType() == titleType
                    && movieYear >= start && movieYear <= end){
                Map<Genre, Integer> innerMap = result.get(movieYear);
                for(Genre genre: movie.getGenres()){
                    if(innerMap.containsKey(genre)){
                        innerMap.put(genre, innerMap.get(genre) + 1);
                    } else{
                        innerMap.put(genre, 1);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Get the movies of a certain type with the most votes. The movies returned should be ordered by descending number
     * of votes, followed by alphabetically by the movie title in case of a tie.
     * @param num number of movies to list
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @return the movies ordered by descending number of votes, then alphabetical by title in case of a tie
     * */
    @Override
    public Collection<Movie> getMoviesMostVotes(int num, String type) {
        // use a comparator that orders Movie's of a type by descending number
        // of votes
        TitleType titleType = TitleType.valueOf(type);   // titleType is TitleType.MOVIE
        List<Movie> result = new LinkedList<>();
        List<Movie> typesList = new LinkedList<>();

        for(Movie movie: this.movieList){
            if (movie.getTitleType() == titleType) {
                typesList.add(movie);
            }
        }

        typesList.sort(new MovieComparatorVotes());
        int i = 0;

        while(result.size() < num) {
            Movie movie = typesList.get(i);
            if (movie.getTitleType() == titleType) {
                result.add(movie);
            }
            i += 1;
        }
        return result;
    }

    /**
     * For a range of inclusive years, get the num top rated movies for each year. The collection returned should have
     * the movies for each year ordered by descending rating, and using the movie title alphabetically to resolve ties.
     * @param num number of top movies
     * @param type the movie type, e.g. "MOVIE", "TV_SHOW", etc.
     * @param start the start year (inclusive)
     * @param end the end year (inclusive)
     * @return the map is keyed by year from start to end inclusive, and the values for each year are the movies
     * ordered by descending rating, using the movie title alphabetically to resolve ties
     */
    @Override
    public Map<Integer, List<Movie>> getMoviesTopRated(int num, String type, int start, int end) {
        Map<Integer, List<Movie>> result = new TreeMap<>();
        TitleType titleType = TitleType.valueOf(type);   // titleType is TitleType.MOVIE
        List<Rating> checkedList = new LinkedList<>();
        for(int i = start; i < end + 1; i++){
            result.put(i, new LinkedList<>());
        }
        for(Movie movie: this.movieList){
            if(movie.getRating().getNumVotes() > MIN_NUM_VOTES_FOR_TOP_RANKED &&
                    movie.getTitleType() == titleType &&
                    start <= movie.getYear() &&
                    movie.getYear() <= end){
                checkedList.add(movie.getRating());
            }
        }

        Collections.sort(checkedList); //Sorts checked list by rating

        for(int i = start; i < end + 1; i++){
            List<Movie> innerList = result.get(i);
            for(Rating rating: checkedList){
                Movie movie = this.movieMap.get(rating.getID());
                if(movie.getYear() == i && innerList.size() < num){
                    innerList.add(movie);
                    result.put(i, innerList);
                }
            }
        }
        return result;
    }
}