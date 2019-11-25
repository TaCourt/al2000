package src.core;

import java.util.*;

import src.gui.ConsoleUserInterface;
import src.persistence.Persistence;
import src.gui.UserInterface;

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

    public VideoClub () {

    }

    public void init () {
        this.initFilmLibrary();
        this.dao.forEachRental((rental) -> {
            if (rental.getReturnDate() == null) this.movieLibrary.removeMovie(rental.getMovie());
        });
        this.currentNonSubRentals = new HashMap<>();
        this.historyNonSubRentals = new HashMap<>();
    }

    private void initFilmLibrary () {
        HashMap<Long, Movie> allMovies = this.dao.loadAllMovies();
        HashMap<Long, Integer> availableMoviesId = this.dao.loadAvailableMovies();
        HashMap<Long, Movie> availableMovies = new HashMap<>();
        HashMap<Long, Movie> al2000Movies = new HashMap<>();

        availableMoviesId.forEach((id, nbOfCopies) -> {
            availableMovies.put(id, allMovies.get(id));
            al2000Movies.put(id, allMovies.get(id));
        });

        List<String> categories = Arrays.asList("Action", "Animation", "Aventure", "Documentaire", "Fantastique",
                "Science-fiction", "Comédie", "Pour adulte", "Western", "Guerre");


        this.movieLibrary = new FilmLibrary(allMovies, availableMovies, al2000Movies, availableMoviesId, categories);
    }

    public void setPersistence(Persistence persistence) {
        this.dao = new DAO();
        this.dao.setPersistence(persistence);
    }

    public void setGui(UserInterface gui) {
        this.gui = gui;
    }

    public void launch() {
        this.init();
        setGui(new ConsoleUserInterface(this));
        gui.welcomePage();
        this.currentSubscriber = null;
    }

    public String[] logIn(String numCarte) {
        this.currentSubscriber = this.dao.loadSubscriber(numCarte);

        String[] infosUser = new String[2];

        if (currentSubscriber == null) {
            infosUser[1] = "";
            infosUser[0] = "";
        } else {
            infosUser[0] = currentSubscriber.getFirstName();
            infosUser[1] = currentSubscriber.getName();
        }
        return infosUser;
    }

    public void logOut() {
        this.currentSubscriber = null;
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

    /**
     * Convertis une map de film en liste de tableau de String
     *
     * @param map la map de film à convertire
     * @return la liste de tableau de String
     */
    public List<String[]> convertList(Map<Long, Movie> map) {
        List<String[]> toReturn = new LinkedList();
        for (Long key : map.keySet()) {
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

    public List<String[]> getMovieByTitle(String title) {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getMovieFromTitle(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained(), title));
        }
        return convertList(movieLibrary.getMovieFromTitle(title));
    }

    public List<String[]> getMoviesByCategory(String category) {
        if (currentSubscriber != null) {
            return convertList(movieLibrary.getMovieByCategory(currentSubscriber.getCategoryRestrained(), currentSubscriber.getMoviesRestrained(), category));
        }
        return convertList(movieLibrary.getMovieByCategory(category));
    }

    /**
     * Location d'un film pour les abonnées : Verifie que l'utilisateur soit bien connecté et que le film existe bien
     * puis effectue la location.
     *
     * @param idMovie : l'identifiant du film que l'utilisateur veut louer
     */
    public boolean rentMovie(String idMovie) {
        Movie m = movieLibrary.getMovie(idMovie);
        if (currentSubscriber != null && m != null && m.isAvailable()) {
            Rental rental = currentSubscriber.rentMovie(m);
            this.dao.save(rental);
            movieLibrary.removeMovie(m);
            return true;
        }
        return false;
    }

    /**
     * Location d'un film pour les non abonnées : Verifie que l'utilisateur n'a pas deja une location en cour
     * puis creer la location et l'ajoute à la liste des locations des utilisateurs non abonées.
     *
     * @param idMovie    : l'identifiant du film que l'utilisateur veut louer
     * @param creditCard : la carte de crédit de l'utilisateur
     */
    public boolean rentMovie(String idMovie, long creditCard) {
        if (currentNonSubRentals.get(creditCard) != null) {
            return false;
        }

        Movie m = movieLibrary.getMovie(idMovie);
        if (m != null && m.isAvailable()) {
            Rental rental = new Rental(m);
            currentNonSubRentals.put(creditCard, rental);
            rental.setRentingDateToNow();
            this.dao.save(rental);
            movieLibrary.removeMovie(m);
        }
        return true;
    }

    /**
     * Retour d'un film pour des abonées. Vérifie que l'utilisateur est bien connecté puis effectue le retour.
     *
     * @param idMovieReturned
     * @return le prix de la location
     */
    public Double returnMovie(String idMovieReturned) {
        Movie m = movieLibrary.getMovie(idMovieReturned);
        if (currentSubscriber != null) {
            movieLibrary.addMovie(m);
            return currentSubscriber.returnMovie(m);
        }
        return null;
    }

    /**
     * Retour d'un film pour des non abonées. Supprime la location correspondante de la liste des locations des utilisateurs non abonées.
     * Ajoute la location à l'historique des locations des utilisateurs non abonées.
     *
     * @param idMovieReturned : l'identifiant du film que l'utilisateurs rend
     * @param creditCard      : la carte de crédit de l'utilisaetur
     * @return le prix de la location
     */
    public Double returnMovie(String idMovieReturned, long creditCard) {
        Movie m = movieLibrary.getMovie(idMovieReturned);
        Rental oldRental = currentNonSubRentals.get(creditCard);
        if( oldRental == null )
            return Double.parseDouble("-1");
        oldRental.setReturnDateToNow();
        currentNonSubRentals.remove(creditCard);
        historyNonSubRentals.put(creditCard, oldRental);
        movieLibrary.addMovie(m);
        return oldRental.getPrice();

    }

    public boolean exists(String identifiant) {
        if (identifiant == null || identifiant.isEmpty() || !identifiant.matches("[0-9]*") ) {
            return false;
        }
        Movie movie = movieLibrary.getMovie(identifiant);
        if (movie == null) {
            return false;
        } else {
            return true;
        }
    }

    public void restrictCategoryForChild(String childId,String chosenCategory){
        AdultSubscriber parent = (AdultSubscriber) currentSubscriber;
        List<ChildSubscriber> children = parent.getChildren();
        for (ChildSubscriber child : children){
            if(child.getSubscriberId() == UUID.fromString(childId)){
                child.restrictMovieByCategory(chosenCategory);
                this.dao.save(child);
            }
        }
    }

    public Map<String,String> getChildrenNames(){
        List<ChildSubscriber> children = ((AdultSubscriber) currentSubscriber).getChildren();
        Map<String,String> names = new HashMap<>();
        for(ChildSubscriber child : children){
            names.put(child.getSubscriberId().toString(),child.getFirstName()+" "+child.getName());
        }
        return names;
    }

    public List<String> getCategories() {
        return movieLibrary.getCategoriesSet();
    }

    public void restrictCategory(String chosenCategory) {
        currentSubscriber.restrictMovieByCategory(chosenCategory);
        this.dao.save(currentSubscriber);
    }

    public String createNewSubscriber(String[] userData) {
        AdultSubscriberFactory userFactory = new AdultSubscriberFactory();
        Subscriber subscriber = userFactory.makeUser(userData[0], userData[1], userData[2]);
        this.dao.save((AdultSubscriber) subscriber);
        return subscriber.getSubscriberId().toString();
    }

    public void createNewChildSubscriber(String[] userData) {
        ChildSubscriberFactory userFactory = new ChildSubscriberFactory();
        Subscriber subscriber = userFactory.makeUser(userData[0], userData[1], (AdultSubscriber) currentSubscriber);
        this.dao.save((ChildSubscriber) subscriber);
    }

    public boolean canAccessChildRestrictionPage() {
        if (currentSubscriber != null)
            return currentSubscriber.canAccessChildRestrictionPage();
        return false;
    }
    public boolean canAccessChildAccountCreationPage(){
        if (currentSubscriber != null){
            return currentSubscriber.canAccessChildAccountCreationPage();
        }
        return false;
    }
}