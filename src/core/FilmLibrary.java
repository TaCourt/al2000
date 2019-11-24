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

    public FilmLibrary(HashMap<Long, Movie> cyberVideoMovie, HashMap<Long, Movie> availableMovies, HashMap<Long, Movie> al2000Movies, HashMap<Long, Integer> nbMovieCopies, List<String> categoriesSet) {
        this.cyberVideoMovies = cyberVideoMovie;
        this.availableMovies = availableMovies;
        this.al2000Movies = al2000Movies;
        this.nbMovieCopies = nbMovieCopies;
        this.categoriesSet = categoriesSet;
    }

    /**
     * Filtre les films de l'al2000 en ne renvoyant pas les films dont la catégorie a été restrainte ou les films dont le titre a été restraint
     */
    private Map<Long, Movie> filter(List<String> restrainedCategory, List<String> restrainedMovie, Map<Long, Movie> list) {
        Map<Long, Movie> toReturn = new HashMap();
        for (Long key : list.keySet()) {
            Movie currentMovie = list.get(key);
            if (!restrainedCategory.contains(currentMovie.getCategory()) && !restrainedMovie.contains(currentMovie.getTitle())) {
                toReturn.put(key, list.get(key));
            }
        }
        return toReturn;
    }

    /**
     * Recherche parmi les films de l'al2000 en filtrant sur une valeur en particulière d'un des attributs suivant : titre, categorie ou langue
     */
    private Map<Long, Movie> search(String component, String componentValue) {
        Map<Long, Movie> toReturn = new HashMap();
        switch(component) {
            case "titre" :
                for (Long key : al2000Movies.keySet()) {
                    System.out.println(al2000Movies.get(key).getTitle());
                    System.out.println(componentValue);
                    if (al2000Movies.get(key).getTitle().equals(componentValue)) {
                        toReturn.put(key, al2000Movies.get(key));
                    }
                }
                break;
            case "categorie" :
                for (Long key : al2000Movies.keySet()) {
                    if (al2000Movies.get(key).getCategory().equals(componentValue)) {
                        toReturn.put(key, al2000Movies.get(key));
                    }
                }
                break;
            case "langue" :
                for (Long key : al2000Movies.keySet()) {
                    if (al2000Movies.get(key).getLanguage().equals(componentValue)) {
                        toReturn.put(key, al2000Movies.get(key));
                    }
                }
                break;
                default :
                    System.out.println("Merci de choisir le paramètre de recherche entre : titre | categorie | langue");
                    break;
        }
        return toReturn;
    }


    public Map<Long, Movie> getCyberVideoMovies(List<String> restrainedCategory, List<String> restrainedMovie) {
        return filter(restrainedCategory, restrainedMovie, cyberVideoMovies);
    }

    public Map<Long, Movie> getCyberVideoMovies() {
        return cyberVideoMovies;
    }


    public Map<Long, Movie> getAl2000Movies(List<String> restrainedCategory, List<String> restrainedMovie) {
        return filter(restrainedCategory, restrainedMovie, al2000Movies);
    }

    public Map<Long, Movie> getAl2000Movies() {
        return al2000Movies;
    }


    public Map<Long, Movie> getAvailableMovies(List<String> restrainedCategory, List<String> restrainedMovie) {
        return filter(restrainedCategory, restrainedMovie, availableMovies);
    }

    public Map<Long, Movie> getAvailableMovies() {
        return availableMovies;
    }

    public Map<Long, Movie> getMovieFromTitle(List<String> restrainedCategory, List<String> restrainedMovie, String title) {
        Map<Long, Movie> toReturn = search("titre", title);
        return filter(restrainedCategory, restrainedMovie, toReturn);
    }

    public Map<Long, Movie> getMovieFromTitle(String title) {
        return search("titre", title);
    }


    public Map<Long, Movie> getMovieByCategory(List<String> restrainedCategory, List<String> restrainedMovie, String category) {
        Map<Long, Movie> toReturn = search("categorie", category);
        return filter(restrainedCategory, restrainedMovie, toReturn);
    }

    public Map<Long, Movie> getMovieByCategory(String category) {
        return search("categorie", category);
    }

    public Movie getMovie(String id) {
        return al2000Movies.get(Long.parseLong(id));
    }

    public List<String> getCategoriesSet() {
        return this.categoriesSet;
    }

    /**
     * Ajoute un exemplaire de film à la filmothèque.
     * Si le nombre d'exemplaire de ce film était à 0 alors on l'incrémente et on l'ajoute dans la liste des films disponible
     * @param m est le film dont l'exemplaire est à ajouter
     */
    public void addMovie(Movie m) {
        Integer nbCopies = nbMovieCopies.get(m.getMovieId());
        if (nbCopies == 0) {
            availableMovies.put(m.getMovieId(), m);
            al2000Movies.get(m.getMovieId()).setAvailability(true);
        }
        nbCopies++;

    }

    /**
     * Retire un exemplaire de film à la filmothèque.
     * Si le nombre d'exemplaire passe à 0, on retire le film des films disponible
     * @param m est le film dont l'exemplaire est à retirer.
     * @return un booleen correspondant à la validation de la méthode : false il y a eu un problème, true tout s'est bien passé
     */
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

}
