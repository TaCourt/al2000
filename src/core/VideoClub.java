package src.core;

import java.util.List;
import src.gui.ConsoleUserInterface;
import src.persistence.Persistence;
import src.gui.UserInterface;

import java.util.LinkedList;
import java.util.Map;

public class VideoClub {
    private VideoClubDAO dao;
    private UserInterface gui;

    private List<Technician> technicians;
    private SubscriberFactory factory;
    private FilmLibrary movieLibrary;
    private Subscriber currentSubscriber;
    private int fineCost;
    private Map<Long, Rental> currentNonSubRentals;
    private Map<Long, Rental> historyNonSubRentals;

    public VideoClub() {
        gui = new ConsoleUserInterface(this);
    }
    public void setPersistence(Persistence persistence) {
        this.dao = new DAO();
        this.dao.setPersistence(persistence);
    }

    public void launch() {

    }

    public void logIn (String subscriberId) {
        this.currentSubscriber = this.dao.loadSubscriber(subscriberId);
    }

    public void addTechnicians(Technician technician) {
        this.technicians.add(technician);
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
        if (currentSubscriber != null && m != null) {
            currentSubscriber.rentingMovie(m);
        }
    }

    public void rentMovie(String idMovie, long creditCard) {
        if (currentNonSubRentals.get(creditCard) != null) {
            System.out.println("Vous avez deja loué un film chez nous, veuillez le rendre avant d'en louer un nouveau svp");
        }

        Movie m = movieLibrary.getMovie(idMovie);
        if (m != null) {
            Rental rental = new Rental(m);
            currentNonSubRentals.put(creditCard, rental);
            this.dao.saveRental(rental);
            movieLibrary.removeMovie(m);
        }
    }

    public void returnMovie(String idMovieReturned, long creditCard) {
        Movie m = movieLibrary.getMovie(idMovieReturned);
        Rental oldRental = currentNonSubRentals.get(creditCard);
        currentNonSubRentals.remove(creditCard);
        historyNonSubRentals.put(creditCard, oldRental);
        movieLibrary.addMovie(m);

    }

    public void returnMovie(String idMovieReturned) {
        Movie m = movieLibrary.getMovie(idMovieReturned);
        if (currentSubscriber != null) {
            currentSubscriber.returnMovie(m);
            movieLibrary.addMovie(m);
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
        currentSubscriber.restrainMovieByCategory(chosenCategory);
    }

    public void createNewSubscriber(String[] userData) {
        AdultSubscriberFactory userFactory = new AdultSubscriberFactory();
        Subscriber subscriber = userFactory.makeUser(userData[0], userData[1], userData[2]);
        this.dao.save((AdultSubscriber) subscriber);
    }

    public void createNewChildSubscriber(String[] userData) {
        ChildSubscriberFactory userFactory = new ChildSubscriberFactory();
        Subscriber subscriber = userFactory.makeUser(userData[0], userData[1], (AdultSubscriber) currentSubscriber);
        this.dao.save((ChildSubscriber) subscriber);
    }
}