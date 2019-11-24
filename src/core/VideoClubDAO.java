package src.core;

import src.persistence.Persistence;

import java.util.HashMap;
import java.util.function.Consumer;

public interface VideoClubDAO {
        void setPersistence(Persistence persistence);
        void save(Subscriber subscriber);
        void save(AdultSubscriber adultSubscriber);
        void save(ChildSubscriber childSubscriber);
        Subscriber loadSubscriber (String id);

        void saveMovie (Movie movie);
        Movie loadMovie (String id);
        HashMap<Long, Movie> loadAllMovies();
        HashMap<Long, Integer> loadAvailableMovies ();
        void saveAvailableMovie (Movie movie, boolean isAvailable, int numberOfCopies);

        Rental loadRental(String currentRentalId);
        void saveRental(Rental rental);
        void forEachRental(Consumer<Rental> callback);
}
