package src.persistence;

import java.util.HashMap;
import java.util.Set;

    public interface Persistence {

    /**
     * Sauvegarde un utilisateur dans la base de données des abonnés
     * @param subscriberDetails map contenant les détails d'un abonné
     */
    void saveSubscriber (HashMap<String, String> subscriberDetails);

    /**
     * Sauvegarde un film dans la base de données des films
     * @param movieDetails map contenant les détails d'un film
     */
    void saveMovie (HashMap<String, String> movieDetails);

    /**
     * Sauvegarde un location dans la base de données des locations
     * @param rentalDetails map contenant les détails d'une location
     */
    void saveRental (HashMap<String, String> rentalDetails);

    /**
     * Ajoute/supprime un film dans la base de données des films disponibles
     * @param id id du film à sauvegarder
     * @param remove indique si le film est disponible physiquement ou non
     * @param numberOfCopies nombre de copies restantes
     */
    void saveAvailableMovie (String id, boolean remove, int numberOfCopies);

    /**
     * Charge la location associée à un id
     * @param id id de l'abonné à charger
     * @return map contenant les détails de l'abonné qui a été chargé
     */
    HashMap<String, String> loadSubscriber(String id);

    /**
     * Charge le film associé à un id
     * @param id id du film à charger
     * @return map contenant les détails du film qui a été chargé
     */
    HashMap<String, String> loadMovie(String id);

    /**
     * Charge une location associée à un id
     * @param id id de la location à charger
     * @return map contenant les détails de la location qui a été chargée
     */
    HashMap<String, String> loadRental(String id);

    /**
     * Charge tous les films disponibles physiquement
     * @return un tableau de string contenant les id des films disponibles physiquement
    */
    HashMap<Long, Integer> getAvailableMovies();

    /**
     * Charge les ids de toutes les locations
     * @return Set des ids de toutes les locations
     */
    Set<String> loadAllRentalIds();

    /**
     * Charge les ids de tous les films
     * @return Set des ids de tous les films
     */
    Set<String> loadAllMoviesIds();
}
