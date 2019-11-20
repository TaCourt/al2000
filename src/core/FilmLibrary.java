package src.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FilmLibrary{
    private Map<Long, Movie> cyberVideoMovies;
    private Map<Long, Movie> al2000Movies;
    private Map<Long, Movie> availableMovies;
    private Map<Long, Integer> nbMovieCopies;
    private List<String> categoriesSet;

    public FilmLibrary(HashMap<Long, Movie> cyberVideoMovie, HashMap<Long, Movie> availableMovies, HashMap<Long, Movie> al2000Movies, int nbExemplaireFilm, List<String> categoriesSet) {
        this.cyberVideoMovies = cyberVideoMovie;
        this.availableMovies = availableMovies;
        this.al2000Movies = al2000Movies;
        this.categoriesSet = categoriesSet;
    }

    public Map<Long, Movie> getCyberVideoMovies() {
        return this.cyberVideoMovies;
    }

    public Map<Long, Movie> getAl2000Movies() {
        return this.al2000Movies;
    }

    public Map<Long, Movie> getAvailableMovies() {
        return this.availableMovies;
    }

    public Map<Long, Movie> getMovieFromTitle(String titre) {
        Map<Long, Movie> toReturn = new HashMap();
        for (Long key : al2000Movies.keySet()) {
            if (al2000Movies.get(key).getTitle().equals(titre)) {
                toReturn.put(key, al2000Movies.get(key));
            }
        }
        return toReturn;
    }

    public Movie getMovie(String id) {
        return al2000Movies.get(Long.parseLong(id));
    }

    public Map<Long, Movie> getMovieByCategory(String category) {
        Map<Long, Movie> toReturn = new HashMap();
        for (Long key : al2000Movies.keySet()) {
            if (al2000Movies.get(key).getCategory().equals(category)) {
                toReturn.put(key, al2000Movies.get(key));
            }
        }
        return toReturn;
    }

    public void addMovie(Movie m) {
        Integer nbCopies = nbMovieCopies.get(m.getMovieId());
        if (nbCopies == 0) {
            availableMovies.put(m.getMovieId(), m);
            al2000Movies.get(m.getMovieId()).setAvailability(true);
        }
        nbCopies++;

    }

    public boolean removeMovie(Movie m) {
        Integer nbCopies = nbMovieCopies.get(m.getMovieId());
        if (nbCopies < 1) {
            return false;
        }
        nbMovieCopies.get(m.getMovieId());
        nbCopies--;
        if (nbCopies == 0) {
            availableMovies.remove(m.getMovieId());
            al2000Movies.get(m.getMovieId()).setAvailability(false);
        }
        return true;
    }

    public List<String> getCategoriesSet() {
        return this.categoriesSet;
    }

}
