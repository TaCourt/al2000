package src.core;

import java.util.List;
import org.json.simple.parser.ParseException;
import src.gui.ConsoleUserInterface;
import src.persistence.JSONPersistence;
import src.persistence.Persistence;
import src.gui.UserInterface;

import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class VideoClub {
    public enum ErrorState {DB_NOT_LOADED};
    private ErrorState errorState = null;

    private Persistence persistence;
    private UserInterface gui;

    private final String ADULT_SUBSCRIBER_ACCOUNT = "adultSubscriber";
    private final String CHILD_SUBSCRIBER_ACCOUNT = "childSubscriber";

    private List<Technician> technicians;
    private UserFactory factory;
    private FilmLibrary movieLibrary;
    private LambdaUser defaultUser;
    private Subscriber currentSubscriber;
    private int fineCost;

    public VideoClub() {
        try {
            this.persistence = new JSONPersistence();
        } catch (IOException | ParseException e) {
            this.errorState = ErrorState.DB_NOT_LOADED;
            System.out.println(e);
        }

        gui = new ConsoleUserInterface(this);
    }

    public void launch() {

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

    public Subscriber loadSubscriber(String id) {
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

    private Movie loadMovie(String movieRestrainedId) {
        // TODO
        return null;
    }

    private Rental loadRental(String currentRentalId) {
        // TODO
        return null;
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
            return convertList(movieLibrary.getMovieFromTitle(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained(), title));
        }
        return convertList(movieLibrary.getMovieFromTitle(title));
    }

    public List<String[]> getMoviesByCategory(String category) {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getMovieByCategory(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained(), category));
        }
        return convertList(movieLibrary.getMovieFromTitle(category));
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


    public void rentMovie(String idMovie) {
        Movie m = movieLibrary.getMovie(idMovie);
        if (currentSubscriber != null) {
            currentSubscriber.rentingMovie(m);
        } else if (defaultUser != null) {
            defaultUser.rentingMovie(m);
        }
    }

    public void returnMovie(Movie m) {
        Movie available = movieLibrary.getAvailableMovies().get(m.getMovieId());
        if (defaultUser != null && available == null) {
            defaultUser.returnMovie(m);
            movieLibrary.addMovie(m);
        }

    }

    public void returnMovie(String idMovieReturned) {
        Movie m = movieLibrary.getMovie(idMovieReturned);
        if (currentSubscriber != null) {
            currentSubscriber.returnMovie(m);
        }

    }


    public String[] login(String numCarte) {
//        User user = loadUser(numCarte);
//        String[] infosUser = new String[2];
//
//        if( user == null){
//            infosUser[1] = "";
//            infosUser[0] = "";
//        }else{
//            infosUser[0] = user.prenom;
//            infosUser[1] = user.nom;
//        }
//        return infosUser;
        return new String[3];
    }


    public boolean exists(String identifiant) {
        Movie movie = movieLibrary.getMovie(identifiant);
        if (movie == null) {
            return false;
        } else {
            return true;
        }
    }

    public String[] getMovieFromTitle(String title) {
//        Movie movie = movieList.get(0); // recherche de la movie
//        String [] movieData = new String[9];
//        movieData[0] = movie.affiche;
//        movieData[1] = movie.titre;
//        movieData[2] = movie.categorie;
//        movieData[3] = movie.synopsis;
//        movieData[4] = movie.duree.toString();
//        movieData[5] = movie.langue;
//        movieData[6] = movie.acteurs;
//        movieData[7] = movie.realisateurs;
//        movieData[8] = movie.identifiant;
        return new String[1];
    }

    public List<String> getCategories() {
        return movieLibrary.getCategoriesSet();
    }

    public List<String[]> getMoviesOfCategory(String category) {
        List<String[]> movieListData = new LinkedList<>();
//        for( Movie movie : movieList){
//            String [] movieData = new String[9];
//            movieData[0] = movie.affiche;
//            movieData[1] = movie.titre;
//            movieData[2] = movie.categorie;
//            movieData[3] = movie.synopsis;
//            movieData[4] = movie.duree.toString();
//            movieData[5] = movie.langue;
//            movieData[6] = movie.acteurs;
//            movieData[7] = movie.realisateurs;
//            movieData[8] = movie.identifiant;
//            movieListData.add(movieData);
//        }
        return movieListData;
    }


    public void restrictCategory(String chosenCategory) {
        Subscriber sub = currentSubscriber;
        sub.restrainMovieByCategory(chosenCategory);
    }

    public void createNewSubscriber(String[] userData) {
        AdultSubscriberFactory userFactory = new AdultSubscriberFactory();
        User user = userFactory.makeUser(userData[0], userData[1], userData[2]);
        save((AdultSubscriber) user);
    }

    public void createNewChildSubscriber(String[] userData) {
        ChildSubscriberFactory userFactory = new ChildSubscriberFactory();
        User user = userFactory.makeUser(userData[0], userData[1], (AdultSubscriber) currentSubscriber);
        save((AdultSubscriber) user);
    }
}