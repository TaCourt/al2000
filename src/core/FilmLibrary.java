package src.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class FilmLibrary{
    private Map<UUID, Movie> cyberVideoMovies;
    private Map<UUID, Movie> al2000Movies;
    private Map<UUID, Movie> availableMovies;
    private Map<UUID, Integer> nbMovieCopies;

    public FilmLibrary(HashMap<UUID, Movie> cyberVideoMovie, HashMap<UUID, Movie> availableMovies, HashMap<UUID, Movie> al2000Movies, int nbExemplaireFilm){
        this.cyberVideoMovies = cyberVideoMovie;
        this.availableMovies = availableMovies;
        this.al2000Movies = al2000Movies;
    }

    public Map<UUID, Movie> getCyberVideoMovies() {
        return this.cyberVideoMovies;
    }

    public Map<UUID, Movie> getAl2000Movies() {
        return this.al2000Movies;
    }

    public Map<UUID, Movie> getAvailableMovies() {
        return this.availableMovies;
    }

    public void addMovie(Movie m) {
        Integer nbCopies = nbMovieCopies.get(m.getMovieID());
        if (nbCopies == 0) {
            availableMovies.put(m.getMovieID(), m);
        }
        nbCopies++;

    }

    public boolean removeMovie(Movie m) {
        Integer nbCopies = nbMovieCopies.get(m.getMovieID());
        if (nbCopies < 1) {
            return false;
        }
        nbMovieCopies.get(m.getMovieID());
        nbCopies--;
        if (nbCopies == 0) {
            availableMovies.remove(m.getMovieID());
        }
        return true;
    }




}
