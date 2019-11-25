package src.core;

import src.persistence.Persistence;

import java.util.HashMap;
import java.util.function.Consumer;

public interface VideoClubDAO {
        /**
         *
         * @param persistence
         */
        void setPersistence(Persistence persistence);

        /**
         *
         * @param subscriber
         */
        void save(Subscriber subscriber);

        /**
         *
         * @param adultSubscriber
         */
        void save(AdultSubscriber adultSubscriber);

        /**
         *
         * @param childSubscriber
         */
        void save(ChildSubscriber childSubscriber);

        /**
         *
         * @param id
         * @return
         */
        Subscriber loadSubscriber (String id);

        /**
         *
         * @param movie
         */
        void saveMovie (Movie movie);

        /**
         *
         * @param id
         * @return
         */
        Movie loadMovie (String id);

        /**
         *
         * @return
         */
        HashMap<Long, Movie> loadAllMovies();

        /**
         *
         * @return
         */
        HashMap<Long, Integer> loadAvailableMovies ();

        /**
         *
         * @param movie
         * @param isAvailable
         * @param numberOfCopies
         */
        void saveAvailableMovie (Movie movie, boolean isAvailable, int numberOfCopies);

        /**
         *
         * @param currentRentalId
         * @return
         */
        Rental loadRental(String currentRentalId);

        /**
         *
         * @param rental
         */
        void save(Rental rental);

        /**
         *
         * @param callback
         */
        void forEachRental(Consumer<Rental> callback);
}
