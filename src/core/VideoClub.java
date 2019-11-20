package src.core;

import java.util.List;
import org.json.simple.parser.ParseException;
import src.persistence.JSONPersistence;
import src.persistence.Persistence;

import java.io.IOException;
import java.util.*;

public class VideoClub {
    public enum ErrorState {DB_NOT_LOADED};
    private ErrorState errorState = null;

    private Persistence persistence;

    private final String LAMBDA_USER_ACCOUNT = "lambdaUser";
    private final String ADULT_SUBSCRIBER_ACCOUNT = "adultSubscriber";
    private final String CHILD_SUBSCRIBER_ACCOUNT = "childSubscriber";

    private List<Technician> technicians;
    private UserFactory factory;
    private FilmLibrary movieLibrary;
    private LambdaUser defaultUser;
    private Subscriber currentSubscriber;
    private int fineCost;

    public VideoClub () {
        try {
            this.persistence = new JSONPersistence();
        }
        catch (IOException | ParseException e) {
            this.errorState = ErrorState.DB_NOT_LOADED;
        }
    }

    private void save (Subscriber subscriber, HashMap<String, String> userDetails) {
        final StringJoiner joinedCategoryRestrained = new StringJoiner(",");
        subscriber.getCategoryRestrained().forEach((String category) -> {joinedCategoryRestrained.add(category);});

        final StringJoiner joinedCurrentRentedMovies = new StringJoiner(",");
        subscriber.getCurrentRentedMovies().forEach((Rental rental) -> {joinedCurrentRentedMovies.add(rental.getRentalId().toString());});

        final StringJoiner joinedHistory = new StringJoiner(",");
        subscriber.getHistory().forEach((Rental rental) -> {joinedHistory.add(rental.getRentalId().toString());});

        final StringJoiner joinedMoviesRestrained = new StringJoiner(",");
        subscriber.getMoviesRestrained().forEach((String movie) -> {joinedMoviesRestrained.add(movie);});

        userDetails.put("name", subscriber.getName());
        userDetails.put("firstName", subscriber.getFirstName());
        userDetails.put("creditCard", Long.toString(subscriber.getCreditCard()));
        userDetails.put("balanceSubscriberCard", Double.toString(subscriber.getBalanceSubscriberCard()));
        userDetails.put("categoryRestrained", joinedCategoryRestrained.toString());
        userDetails.put("currentRentedMovies", joinedCurrentRentedMovies.toString());
        userDetails.put("history", joinedHistory.toString());
        userDetails.put("moviesRestrained", joinedMoviesRestrained.toString());
        userDetails.put("maxMovieRented", Integer.toString(subscriber.getMaxMovieRented()));
        userDetails.put("UUID", subscriber.getSubscriberId().toString());

        this.persistence.saveUser(userDetails);
    }

    public void save (AdultSubscriber adultSubscriber) {
        HashMap<String, String> userDetails = new HashMap<String, String>();

        final StringJoiner childrenIds = new StringJoiner(",");
        adultSubscriber.getChildren().forEach((ChildSubscriber child) -> {childrenIds.add(child.getSubscriberId().toString());});

        userDetails.put("account", this.ADULT_SUBSCRIBER_ACCOUNT);
        userDetails.put("children", childrenIds.toString());

        this.save(adultSubscriber, userDetails);
    }

    public void save (ChildSubscriber childSubscriber) {
        HashMap<String, String> userDetails = new HashMap<String, String>();

        userDetails.put("account", this.CHILD_SUBSCRIBER_ACCOUNT);
        userDetails.put("parent", childSubscriber.getParent().getSubscriberId().toString());

        this.save(childSubscriber, userDetails);
    }

    public User loadUser (String id) {
        HashMap<String, String> userDetails = this.persistence.loadUser(id);

        if (userDetails == null) {
            return null;
        }

        User user = null;
        String account = userDetails.get("account");
        if (this.LAMBDA_USER_ACCOUNT.equals(account)) {
            user = new LambdaUser(UUID.fromString(userDetails.get("UUID")), Long.parseLong(userDetails.get("creditCard")));
        }
        else if (this.ADULT_SUBSCRIBER_ACCOUNT.equals(account)) {
            user = new AdultSubscriber(UUID.fromString(userDetails.get("UUID")),
                                        Long.parseLong(userDetails.get("creditCard")),
                                        userDetails.get("name"),
                                        userDetails.get("firstName"),
                                        Double.parseDouble(userDetails.get("balanceSubscriberCard")));
        }
        else if (this.CHILD_SUBSCRIBER_ACCOUNT.equals(account)) {
            user = new ChildSubscriber(UUID.fromString(userDetails.get("UUID")),
                                        Long.parseLong(userDetails.get("creditCard")),
                                        userDetails.get("name"),
                                        userDetails.get("firstName"),
                                        Double.parseDouble(userDetails.get("balanceSubscriberCard")),
                                        (AdultSubscriber) this.loadUser(userDetails.get("parent")));
        }

        return user;
    }

    public void addTechnicians(Technician technician) {
        this.technicians.add(technician);
    }

    public LambdaUser getDefaultUser() {
        return defaultUser;
    }

    public Subscriber getCurrentSubscriber() {
        return currentSubscriber;
    }

    public int getFineCost() {
        return fineCost;
    }

    public void setFineCost(int fineCost) {
        this.fineCost = fineCost;
    }

    public List<String[]> getCyberVideoMovie() {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getCyberVideoMovies(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained()));
        }
        return convertList(movieLibrary.getCyberVideoMovies());
    }

    public List<String[]> getAvailableMovies() {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getAvailableMovies(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained()));
        }
        return convertList(movieLibrary.getAvailableMovies());
    }

    public List<String[]> getAl2000Movies() {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getAl2000Movies(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained()));
        }
        return convertList(movieLibrary.getAl2000Movies());
    }

    public List<String[]> getMovie(String title) {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getMovie(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained(), title));
        }
        return convertList(movieLibrary.getMovie(title));
    }

    public List<String[]> getMoviesByCategory(String category) {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getMovieByCategory(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained(), category));
        }
        return convertList(movieLibrary.getMovie(category));
    }

    public List<String[]> convertList(Map<Long, Movie> map) {
        List<String[]> toReturn = new LinkedList();
        for (Long key : movieLibrary.getCyberVideoMovies().keySet()) {
            String[] movie = new String[9];
            movie[0] = map.get(key).getAffiche();
            movie[1] = map.get(key).getTitle();
            movie[2] = map.get(key).getCategory();
            movie[3] = map.get(key).getSynopsis();
            movie[4] = map.get(key).getDuration().toString();
            movie[5] = map.get(key).getLanguage();
            movie[6] = map.get(key).getActor();
            movie[7] = map.get(key).getDirector();
            movie[8] = map.get(key).getMovieId().toString();
            toReturn.add(movie);
        }
        return toReturn;
    }

    public void rentingMovie(Movie m) {
        Movie available = movieLibrary.getAvailableMovies().get(m.getMovieId());
        if (defaultUser != null && available != null) {
            defaultUser.rentingMovie(m);
            movieLibrary.removeMovie(m);
        }
    }

    public void returnMovie(Movie m) {
        Movie available = movieLibrary.getAvailableMovies().get(m.getMovieId());
        if (defaultUser != null && available == null) {
            defaultUser.returnMovie(m);
            movieLibrary.addMovie(m);
        }
    }
}
