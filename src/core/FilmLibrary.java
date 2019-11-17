package src.core;

import java.util.List;

public class FilmLibrary{
    private List<Movie> cyberVideoMovies;
    private List<Movie> availableMovies;
    private int nbExemplaireFilm;

    public FilmLibrary(List<Movie> cyberVideoMovie, List<Movie> availableMovies, int nbExemplaireFilm){
        this.cyberVideoMovies = cyberVideoMovie;
        this.availableMovies = availableMovies;
        this.nbExemplaireFilm = nbExemplaireFilm;
    }

    public List<Movie> getCyberVideoMovies() {
        return this.cyberVideoMovies;
    }

    public List<Movie> getAvailableMovies() {
        return this.availableMovies;
    }
}
