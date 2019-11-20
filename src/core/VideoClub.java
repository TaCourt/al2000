package src.core;

import java.util.List;
import org.json.simple.parser.ParseException;
import src.persistence.JSONPersistence;
import src.persistence.Persistence;

import java.io.IOException;
import java.util.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
    private User currentUser;
    private int fineCost;

    public VideoClub () {
        try {
            this.persistence = new JSONPersistence();
        }
        catch (IOException | ParseException e) {
            this.errorState = ErrorState.DB_NOT_LOADED;
        }
    }

    public void save (LambdaUser lambdaUser) {
        HashMap<String, String> userDetails = new HashMap<String, String>();
        Rental rental = lambdaUser.getCurrentRental();
        userDetails.put("currentRental", rental == null ? "" : rental.getRentalId().toString());
        userDetails.put("creditCard", Long.toString(lambdaUser.getCreditCard()));

        userDetails.put("account", this.LAMBDA_USER_ACCOUNT);
        userDetails.put("UUID", lambdaUser.getUserId().toString());

        this.persistence.saveUser(userDetails);
    }

    private void save (Subscriber subscriber, HashMap<String, String> userDetails) {
        final StringJoiner joinedCategoryRestrained = new StringJoiner(",");
        subscriber.getCategoryRestrained().forEach((String category) -> {joinedCategoryRestrained.add(category);});

        final StringJoiner joinedCurrentRentedMovies = new StringJoiner(",");
        subscriber.getCurrentRentedMovies().forEach((Rental rental) -> {joinedCurrentRentedMovies.add(rental.getRentalId().toString());});

        final StringJoiner joinedHistory = new StringJoiner(",");
        subscriber.getHistory().forEach((Rental rental) -> {joinedHistory.add(rental.getRentalId().toString());});

        final StringJoiner joinedMoviesRestrained = new StringJoiner(",");
        subscriber.getMoviesRestrained().forEach((Movie movie) -> {joinedMoviesRestrained.add(movie.getMovieId().toString());});

        userDetails.put("name", subscriber.getName());
        userDetails.put("firstName", subscriber.getFirstName());
        userDetails.put("creditCard", Long.toString(subscriber.getCreditCard()));
        userDetails.put("balanceSubscriberCard", Double.toString(subscriber.getBalanceSubscriberCard()));
        userDetails.put("categoryRestrained", joinedCategoryRestrained.toString());
        userDetails.put("currentRentedMovies", joinedCurrentRentedMovies.toString());
        userDetails.put("history", joinedHistory.toString());
        userDetails.put("moviesRestrained", joinedMoviesRestrained.toString());
        userDetails.put("maxMovieRented", Integer.toString(subscriber.getMaxMovieRented()));
        userDetails.put("UUID", subscriber.getUserId().toString());

        this.persistence.saveUser(userDetails);
    }

    public void save (AdultSubscriber adultSubscriber) {
        HashMap<String, String> userDetails = new HashMap<String, String>();

        final StringJoiner childrenIds = new StringJoiner(",");
        adultSubscriber.getChildren().forEach((ChildSubscriber child) -> {childrenIds.add(child.getUserId().toString());});

        userDetails.put("account", this.ADULT_SUBSCRIBER_ACCOUNT);
        userDetails.put("children", childrenIds.toString());

        this.save(adultSubscriber, userDetails);
    }

    public void save (ChildSubscriber childSubscriber) {
        HashMap<String, String> userDetails = new HashMap<String, String>();

        userDetails.put("account", this.CHILD_SUBSCRIBER_ACCOUNT);
        userDetails.put("parent", childSubscriber.getParent().getUserId().toString());

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

    public User getCurrentUser() {
        return currentUser;
    }

    public int getFineCost() {
        return fineCost;
    }

    public void setFineCost(int fineCost) {
        this.fineCost = fineCost;
    }

    public List<String[]> getCyberVideoMovie() {
        return convertList(movieLibrary.getCyberVideoMovies());
    }

    public List<String[]> getAvailableMovies() {
        return convertList(movieLibrary.getAvailableMovies());
    }

    public List<String[]> getAl2000Movies() {
        return convertList(movieLibrary.getAl2000Movies());
    }

    public List<String[]> getMovie(String title) {
        return convertList(movieLibrary.getMovieFromTitle(title));
    }

    public List<String[]> getMoviesByCategory(String category) {
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

    public void rentingMovie(Movie m) {
        if (currentUser != null) {
            currentUser.rentingMovie(m);
        }
    }

    public void returnMovie(Movie m) {
        if (currentUser != null) {
            currentUser.returnMovie(m);
        }

    }

    public void returnMovie(String idMovieReturned) {
        Movie m = movieLibrary.getMovie(idMovieReturned);
        if (currentUser != null) {
            currentUser.returnMovie(m);
        }

    }


    public String[] login(String numCarte){
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


    public boolean exists(String identifiant){
        return true;
    }

    public String[] getMovieFromTitle(String title){
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
    public void rentMovie(String identifiant){

    }

    public List<String> getCategories(){
        List<String> categories = new LinkedList<>();
        categories.add("Horreur");
        categories.add("Fantastique");
        categories.add("Comédie");
        categories.add("Drame");
        categories.add("Pour Adulte");
        return categories;
    }

    public List<String[]> getMoviesOfCategory(String category){
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

    }

    public void createNewSubscriber(String[] userData) {
//        userData[0] = nom
//        userData[1] = prenom
//        userData[2] = num CB
    }

    public void createNewChildSubscriber(String[] userData) {
//        userData[0] = nom
//        userData[1] = prenom
    }

}
