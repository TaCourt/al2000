package src.core;

import src.persistence.Persistence;

import java.util.HashMap;
import java.util.StringJoiner;
import java.util.UUID;

public class DAO {
    private final String ADULT_SUBSCRIBER_ACCOUNT = "adultSubscriber";
    private final String CHILD_SUBSCRIBER_ACCOUNT = "childSubscriber";

    private Persistence persistence;

    public DAO () {

    }

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

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

    public void save(AdultSubscriber adultSubscriber) {
        HashMap<String, String> subscriberDetails = new HashMap<String, String>();

        final StringJoiner childrenIds = new StringJoiner(",");
        adultSubscriber.getChildren().forEach((ChildSubscriber child) -> {
            childrenIds.add(child.getSubscriberId().toString());
        });

        subscriberDetails.put("account", this.ADULT_SUBSCRIBER_ACCOUNT);
        subscriberDetails.put("children", childrenIds.toString());

        this.save(adultSubscriber, subscriberDetails);
    }

    public void save(ChildSubscriber childSubscriber) {
        HashMap<String, String> subscriberDetails = new HashMap<String, String>();

        subscriberDetails.put("account", this.CHILD_SUBSCRIBER_ACCOUNT);
        subscriberDetails.put("parent", childSubscriber.getParent().getSubscriberId().toString());

        this.save(childSubscriber, subscriberDetails);
    }

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

    private ChildSubscriber loadChildSubscriber(HashMap<String, String> subscriberDetails) {
        return loadChildSubscriber(subscriberDetails, this.loadAdultSubscriber(subscriberDetails));
    }

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

    private AdultSubscriber loadAdultSubscriber(HashMap<String, String> subscriberDetails) {
        final String adultSubscriberId = subscriberDetails.get("UUID");

        AdultSubscriber adultSubscriber = new AdultSubscriber(UUID.fromString(adultSubscriberId),
                Long.parseLong(subscriberDetails.get("creditCard")),
                subscriberDetails.get("name"),
                subscriberDetails.get("firstName"),
                Double.parseDouble(subscriberDetails.get("balanceSubscriberCard")));

        String[] splitString = subscriberDetails.get("children").split(",");
        for (String childId : splitString) {
            adultSubscriber.addChild(this.loadChildSubscriber(this.persistence.loadSubscriber(childId), adultSubscriber));
        }
        /*this.persistence.forEachSubscriber((_id, _userDetails) -> {
            if ((_userDetails).get("account").equals(this.CHILD_SUBSCRIBER_ACCOUNT) && ((_userDetails).get("parent").equals(adultSubscriberId))) {
                adultSubscriber.addChild(this.loadChildSubscriber(_userDetails, adultSubscriber));
            }
        });*/

        loadSubscriber(subscriberDetails, adultSubscriber);
        return adultSubscriber;
    }

    private void loadSubscriber(HashMap<String, String> subscriberDetails, Subscriber subscriber) {
        String[] splitString;
        splitString = subscriberDetails.get("categoryRestrained").split(",");
        for (String category : splitString) {
            subscriber.restrainMovieByTitle(category);
        }

        splitString = subscriberDetails.get("currentRentedMovies").split(",");
        for (String currentRentalId : splitString) {
            subscriber.addRental(this.loadRental(currentRentalId));
        }

        splitString = subscriberDetails.get("history").split(",");
        for (String oldRentalId : splitString) {
            subscriber.addRental(this.loadRental(oldRentalId));
        }

        splitString = subscriberDetails.get("moviesRestrained").split(",");
        for (String movieRestrainedTitle : splitString) {
            subscriber.restrainMovieByTitle(movieRestrainedTitle);
        }
    }


    public void saveMovie (Movie movie) {
        HashMap<String, String> movieDetails = new HashMap<String, String>();

        movieDetails.put("id", movie.getMovieId().toString());
        movieDetails.put("category", movie.getCategory());
        movieDetails.put("title", movie.getTitle());
        movieDetails.put("language", movie.getLanguage());
        movieDetails.put("actor", movie.getActor());
        movieDetails.put("affiche", movie.getAffiche());
        movieDetails.put("director", movie.getDirector());
        movieDetails.put("synopsis", movie.getSynopsis());
        movieDetails.put("duration", movie.getDuration().toString());
        movieDetails.put("available", Boolean.toString(movie.isAvailable()));

        this.persistence.saveMovie(movieDetails);
    }
    public Movie loadMovie(String id) {
        HashMap<String, String> movieDetails = this.persistence.loadSubscriber(id);

        if (movieDetails == null) {
            return null;
        }

        return new Movie(Long.parseLong(movieDetails.get("id")),
            movieDetails.get("affiche"),
            movieDetails.get("titre"),
            movieDetails.get("category"),
            movieDetails.get("synopsis"),
            Integer.parseInt(movieDetails.get("duration")),
            movieDetails.get("langue"),
            movieDetails.get("actor"),
            movieDetails.get("director"),
            false);
    }

    public Rental loadRental(String currentRentalId) {
        HashMap<String, String> rentalDetails = this.persistence.loadRental(currentRentalId);
        if (rentalDetails == null) {
            return null;
        }
        return new Rental(UUID.fromString(rentalDetails.get("UUID")), this.loadMovie(rentalDetails.get(rentalDetails.get("movieId"))));
    }

    public void saveRental(Rental rental) {
        HashMap<String, String> rentalDetails = new HashMap();

        rentalDetails.put("UUID", rental.getRentalId().toString());
        rentalDetails.put("movieId", rental.getMovie().getMovieId().toString());
    }
}
