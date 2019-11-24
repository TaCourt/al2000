package src.core;

import src.persistence.Persistence;

import java.util.List;

public interface VideoClubDAO {
        void setPersistence(Persistence persistence);
        void save(AdultSubscriber adultSubscriber);
        void save(ChildSubscriber childSubscriber);
        Subscriber loadSubscriber (String id);
        void saveMovie (Movie movie);
        Movie loadMovie (String id);
        List<Movie> loadAvailableMovies();
        Rental loadRental(String currentRentalId);
        void saveRental(Rental rental);
    }
