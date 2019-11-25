package src.core;

import src.persistence.Persistence;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;

public class DAO implements VideoClubDAO {
    private final String ADULT_SUBSCRIBER_ACCOUNT = "adultSubscriber";
    private final String CHILD_SUBSCRIBER_ACCOUNT = "childSubscriber";

    private Persistence persistence;

    @Override
    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    /**
     * Sauvegarde la partie commune aux classes qui hérite de Subscriber dans subscriberDetails
     * @param subscriber le Subscriber à sauvegarder
     * @param subscriberDetails La HashMap où sauvegarder les informations du Subscriber
     */
    private void save(Subscriber subscriber, HashMap<String, String> subscriberDetails) {
        final StringJoiner joinedCategoryRestrained = new StringJoiner(",");
        subscriber.getCategoryRestrained().forEach((String category) -> {
            joinedCategoryRestrained.add(category);
        });

        final StringJoiner joinedCurrentRentedMovies = new StringJoiner(",");
        subscriber.getCurrentRentedMovies().forEach((Rental rental) -> {
            joinedCurrentRentedMovies.add(rental.getRentalId().toString());
        });

        final StringJoiner joinedHistory = new StringJoiner(",");
        subscriber.getHistory().forEach((Rental rental) -> {
            joinedHistory.add(rental.getRentalId().toString());
        });

        final StringJoiner joinedMoviesRestrained = new StringJoiner(",");
        subscriber.getMoviesRestrained().forEach((String movieTitle) -> {
            joinedMoviesRestrained.add(movieTitle);
        });

        subscriberDetails.put("name", subscriber.getName());
        subscriberDetails.put("firstName", subscriber.getFirstName());
        subscriberDetails.put("creditCard", Long.toString(subscriber.getCreditCard()));
        subscriberDetails.put("balanceSubscriberCard", Double.toString(subscriber.getBalanceSubscriberCard()));
        subscriberDetails.put("categoryRestrained", joinedCategoryRestrained.toString());
        subscriberDetails.put("currentRentedMovies", joinedCurrentRentedMovies.toString());
        subscriberDetails.put("history", joinedHistory.toString());
        subscriberDetails.put("moviesRestrained", joinedMoviesRestrained.toString());
        subscriberDetails.put("maxMovieRented", Integer.toString(subscriber.getMaxMovieRented()));
        subscriberDetails.put("UUID", subscriber.getSubscriberId().toString());

        this.persistence.saveSubscriber(subscriberDetails);
    }

    @Override
    public void save (Subscriber subscriber) {
        if (subscriber instanceof AdultSubscriber) {
            this.save((AdultSubscriber) subscriber);
        }
        else if (subscriber instanceof AdultSubscriber) {
            this.save((ChildSubscriber) subscriber);
        }
    }

    @Override
    public void save(AdultSubscriber adultSubscriber) {
        HashMap<String, String> subscriberDetails = new HashMap<String, String>();

        StringJoiner childrenIds = new StringJoiner(",");
        adultSubscriber.getChildren().forEach((ChildSubscriber child) -> {
            childrenIds.add(child.getSubscriberId().toString());
        });

        subscriberDetails.put("account", this.ADULT_SUBSCRIBER_ACCOUNT);
        subscriberDetails.put("children", childrenIds.toString());

        this.save(adultSubscriber, subscriberDetails);
    }

    @Override
    public void save(ChildSubscriber childSubscriber) {
        HashMap<String, String> subscriberDetails = new HashMap<String, String>();

        subscriberDetails.put("account", this.CHILD_SUBSCRIBER_ACCOUNT);
        subscriberDetails.put("parent", childSubscriber.getParent().getSubscriberId().toString());

        this.save(childSubscriber, subscriberDetails);
    }

    @Override
    public Subscriber loadSubscriber (String id) {
        HashMap<String, String> subscriberDetails = this.persistence.loadSubscriber(id);

        if (subscriberDetails == null) {
            return null;
        }
        Subscriber subscriber = null;
        String account = subscriberDetails.get("account");

        if (this.ADULT_SUBSCRIBER_ACCOUNT.equals(account)) {
            subscriber = this.loadAdultSubscriber(subscriberDetails);
        } else if (this.CHILD_SUBSCRIBER_ACCOUNT.equals(account)) {
            ChildSubscriber childSubscriber = this.loadChildSubscriber(subscriberDetails);
            loadSubscriber(subscriberDetails, childSubscriber);

            subscriber = childSubscriber;
        }

        return subscriber;
    }


    /**
     * Charge un ChildSubscriber dont le parent n'est pas déjà chargé
     * @param subscriberDetails détails du ChildSubscriber
     * @return le ChildSubscriber associé aux informations de subscriberDetails
     */
    private ChildSubscriber loadChildSubscriber(HashMap<String, String> subscriberDetails) {
        return loadChildSubscriber(subscriberDetails, this.loadAdultSubscriber(subscriberDetails));
    }

    /**
     * Charge un ChildSubscriber dont le parent AdultSubscriber est parent
     * @param subscriberDetails détails du ChildSubscriber
     * @param parent parent AdultSubscriber du ChildSubscriber
     * @return le ChildSubscriber associé aux informations de subscriberDetails
     */
    private ChildSubscriber loadChildSubscriber(HashMap<String, String> subscriberDetails, AdultSubscriber parent) {
        String childId = subscriberDetails.get("UUID");

        ChildSubscriber childSubscriber = new ChildSubscriber(UUID.fromString(childId),
                Long.parseLong(subscriberDetails.get("creditCard")),
                subscriberDetails.get("name"),
                subscriberDetails.get("firstName"),
                Double.parseDouble(subscriberDetails.get("balanceSubscriberCard")),
                parent);
        return childSubscriber;
    }

    /**
     * Charge un AdultSubscriber associé aux informations de subscriberDetails
     * @param subscriberDetails  détails de l'AdultSubscriber
     * @return un AdultSubscriber associé aux informations de subscriberDetails
     */
    private AdultSubscriber loadAdultSubscriber(HashMap<String, String> subscriberDetails) {
        final String adultSubscriberId = subscriberDetails.get("UUID");

        AdultSubscriber adultSubscriber = new AdultSubscriber(UUID.fromString(adultSubscriberId),
                Long.parseLong(subscriberDetails.get("creditCard")),
                subscriberDetails.get("name"),
                subscriberDetails.get("firstName"),
                Double.parseDouble(subscriberDetails.get("balanceSubscriberCard")));

        if (subscriberDetails.get("children").length() != 0) {
            String[] splitString = subscriberDetails.get("children").split(",");
            for (String childId : splitString) {
                adultSubscriber.addChild(this.loadChildSubscriber(this.persistence.loadSubscriber(childId), adultSubscriber));
            }
        }

        this.loadSubscriber(subscriberDetails, adultSubscriber);
        return adultSubscriber;
    }

    /**
     * Charge la partie commune aux classes qui hérite de Subscriber dans subscriberDetails
     * @param subscriberDetails le Subscriber à charger
     * @param subscriber La HashMap où charger les informations du Subscriber
     */
    private void loadSubscriber(HashMap<String, String> subscriberDetails, Subscriber subscriber) {
        String[] splitString;
        if (subscriberDetails.get("categoryRestrained").length() != 0) {
            splitString = subscriberDetails.get("categoryRestrained").split(",");
            for (String category : splitString) {
                subscriber.restrictMovieByCategory(category);
            }
        }

        if (subscriberDetails.get("currentRentedMovies").length() != 0) {
            splitString = subscriberDetails.get("currentRentedMovies").split(",");
            for (String currentRentalId : splitString) {
                subscriber.addRental(this.loadRental(currentRentalId));
            }
        }

        if (subscriberDetails.get("history").length() != 0) {
            splitString = subscriberDetails.get("history").split(",");
            for (String oldRentalId : splitString) {
                subscriber.addRental(this.loadRental(oldRentalId));
            }
        }

        if (subscriberDetails.get("moviesRestrained").length() != 0) {
            splitString = subscriberDetails.get("moviesRestrained").split(",");
            for (String movieRestrainedTitle : splitString) {
                subscriber.restrictMovieByTitle(movieRestrainedTitle);
            }
        }
    }

    @Override
    public void saveMovie (Movie movie) {
        HashMap<String, String> movieDetails = new HashMap<>();

        boolean isAvailable = movie.isAvailable();

        movieDetails.put("id", movie.getMovieId().toString());
        movieDetails.put("category", movie.getCategory());
        movieDetails.put("title", movie.getTitle());
        movieDetails.put("language", movie.getLanguage());
        movieDetails.put("actor", movie.getActor());
        movieDetails.put("affiche", movie.getAffiche());
        movieDetails.put("director", movie.getDirector());
        movieDetails.put("synopsis", movie.getSynopsis());
        movieDetails.put("duration", movie.getDuration().toString());
        movieDetails.put("available", Boolean.toString(isAvailable));

        this.persistence.saveMovie(movieDetails);
    }

    @Override
    public void saveAvailableMovie (Movie movie, boolean isAvailable, int numberOfCopies) {
        this.persistence.saveAvailableMovie(movie.getMovieId().toString(), isAvailable, numberOfCopies);
    }

    @Override
    public Movie loadMovie (String id) {
        HashMap<String, String> movieDetails = this.persistence.loadMovie(id);

        if (movieDetails == null) {
            return null;
        }

        return new Movie(Long.parseLong(movieDetails.get("id")),
            movieDetails.get("affiche"),
            movieDetails.get("title"),
            movieDetails.get("category"),
            movieDetails.get("synopsis"),
            Integer.parseInt(movieDetails.get("duration")),
            movieDetails.get("language"),
            movieDetails.get("actor"),
            movieDetails.get("director"),
            Boolean.valueOf(movieDetails.get("available")));
    }

    @Override
    public HashMap<Long, Movie> loadAllMovies () {
        HashMap<Long, Movie> movies = new HashMap<>();

        this.persistence.loadAllMoviesIds().forEach((id) -> {
            movies.put(Long.valueOf(id), this.loadMovie(id));
        });

        return movies;
    }

    @Override
    public HashMap<Long, Integer> loadAvailableMovies () {
        return this.persistence.getAvailableMovies();
    }

    @Override
    public Rental loadRental(String currentRentalId) {
        HashMap<String, String> rentalDetails = this.persistence.loadRental(currentRentalId);
        if (rentalDetails == null) {
            return null;
        }

        try {
            String returnDate = rentalDetails.get("returnDate");

            return new Rental(UUID.fromString(rentalDetails.get("UUID")),
                    this.loadMovie(rentalDetails.get("movieId")),
                    "".equals(returnDate) ? null : DateFormat.getDateInstance().parse(returnDate),
                    DateFormat.getDateInstance().parse(rentalDetails.get("rentingDate")));
        } catch (ParseException e) {
            return new Rental(UUID.fromString(rentalDetails.get("UUID")),
                    this.loadMovie(rentalDetails.get("movieId")),
                    null, null);
        }
    }

    @Override
    public void save(Rental rental) {
        HashMap<String, String> rentalDetails = new HashMap();

        rentalDetails.put("UUID", rental.getRentalId().toString());
        rentalDetails.put("movieId", rental.getMovie().getMovieId().toString());
        rentalDetails.put("price", rental.getPricePerDay().toString());
        rentalDetails.put("rentingDate", rental.getRentingDate().toString());
        if(rental.getReturnDate() == null){
            rentalDetails.put("returnDate", "");
        }else{
            rentalDetails.put("returnDate", rental.getReturnDate().toString());
        }


        this.persistence.saveRental(rentalDetails);
    }

    @Override
    public void forEachRental(Consumer<Rental> callback) {
        this.persistence.loadAllRentalIds().forEach((id) -> {
            callback.accept(this.loadRental((String) id));
        });
    }
}
