package src.core;

import src.persistence.Persistence;

import java.util.HashMap;
import java.util.function.Consumer;

public interface VideoClubDAO {
        /**
         * Initialise l'objet de persistence
         * @param persistence Instance de l'objet de persistence (~ driver de la base de données)
         */
        void setPersistence(Persistence persistence);

        /**
         * Sauvegarde un Subscriber
         * @param subscriber Instance d'un Subscriber
         */
        void save(Subscriber subscriber);

        /**
         * Sauvegarde un AdultSubscriber
         * @param adultSubscriber Instance d'un AdultSubscriber
         */
        void save(AdultSubscriber adultSubscriber);

        /**
         * Sauvegarde un ChildSubscriber
         * @param childSubscriber Instance d'un ChildSubscriber
         */
        void save(ChildSubscriber childSubscriber);

        /**
         * Charge un Subscriber depuis la persistence en fonction de son id
         * @param id id du Subscriber
         * @return le Subscriber associé à l'id, null sinon
         */
        Subscriber loadSubscriber (String id);

        /**
         * Sauvegarde un Movie
         * @param movie Instance d'un Movie
         */
        void saveMovie (Movie movie);

        /**
         * Charge un Movie depuis la persistence en fonction de son id
         * @param id id du Movie
         * @return Movie associé à l'id, null sinon
         */
        Movie loadMovie (String id);

        /**
         * Charge tous les Movie depuis la persistence
         * @return HashMap de Movie, avec la paire {id: Movie}
         */
        HashMap<Long, Movie> loadAllMovies();

        /**
         * Charge tous les Movies disponible physiquement
         * @return HashMap de Movie, avec la paire {id: Nombre de copies disponibles}
         */
        HashMap<Long, Integer> loadAvailableMovies ();

        /**
         * Met à jour la liste des films disponibles :
         * Si movie est disponible, le sauvegarde avec son nombre de copies disponibles
         * Sinon, le retire de la liste des films disponibles
         * @param movie Movie à mettre à jour dans la liste des films disponibles
         * @param isAvailable S'il est disponible physiquement ou non
         * @param numberOfCopies Nombre de copies disponibles
         */
        void saveAvailableMovie (Movie movie, boolean isAvailable, int numberOfCopies);

        /**
         * Charge une Rental depuis la persistence
         * @param currentRentalId id de la Rental
         * @return Rental associée à l'id, null sinon
         */
        Rental loadRental(String currentRentalId);

        /**
         * Sauvegarde une Rental
         * @param rental Instance d'une Rental
         */
        void save(Rental rental);

        /**
         * Permet d'appeler un callback sur toutes les Rental
         * @param callback fonction callback, prends en paramètre une Rental
         */
        void forEachRental(Consumer<Rental> callback);
}
