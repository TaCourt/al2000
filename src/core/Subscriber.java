package src.core;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Subscriber {
    private long creditCard;
    private UUID subscriberId;
    private String name;
    private String firstName;
    private double balanceSubscriberCard;
    private List<Rental> currentRentedMovies;
    private int maxMovieRented;
    private Double pricePerDay = 4.0;

    private List<Rental> history;

    private List<String> categoryRestrained; //Liste des catégories restreintes à l'utilisateur
    private List<String> moviesRestrained; //Liste des titres de films restreints à l'utilisateur

    public Subscriber(UUID subscriberId, long creditCard, String name, String firstName, double balance) {
        this.creditCard = creditCard;
        this.subscriberId = subscriberId;
        this.name = name;
        this.firstName = firstName;
        this.balanceSubscriberCard = balance;
        this.currentRentedMovies = new ArrayList<>();
        this.maxMovieRented = 3;
        this.moviesRestrained = new ArrayList<>();
        this.categoryRestrained = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    /**
     * Ajoute une location à la liste des locations courantes de l'utilisateur.
     * Si l'utilisateur a atteint la limite du nombre de film à louer simultanement
     * on l'informe.
     * @param m est le film que l'utilisateur veut louer
     */
    public void rentMovie(Movie m) {
        if (currentRentedMovies.size() != maxMovieRented) {
            addRental(m);
        } else {
            System.out.println("Tu as deja loué 3 films..");//A changer par une exception
        }
    }

    /**
     * Met à jour la location en fixant sa date de retour. Retire cette location de la liste des locations courantes
     * Ajoute la location à l'historique des locations. Calcul du cout final de la location en testant si l'abonné a assez de credit sur sa carte
     * @param m est le film à retourner.
     * @return le cout de la location
     */
    public double returnMovie(Movie m) {
        for (Rental r : currentRentedMovies) {
            if (r.getMovie().getMovieId().equals(m.getMovieId())) {
                if (balanceSubscriberCard < r.getPrice()) {
                    System.out.println("Vous n'avez pas assès de credit sur votre carte, veuillez la recharger");
                }
                r.setReturnDate();
                history.add(r);
                return r.getPricePerDay();
            }
        }
        return 0.0;
    }


    private void addRental(Movie m) {
        currentRentedMovies.add(new Rental(UUID.randomUUID(), m, this.pricePerDay));
    }
    public void addRental(Rental r) { currentRentedMovies.add(r); }

    public void restrictMovieByTitle(String title) {
        moviesRestrained.add(title);
    }

    public void restrictMovieByCategory(String category) {
        categoryRestrained.add(category);
    }

    public boolean removeRestrictedMovie(String title) {
        return moviesRestrained.remove(title);
    }

    public boolean removeRestrictedCategory(String category) {
        return moviesRestrained.remove(category);
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public double getBalanceSubscriberCard() {
        return balanceSubscriberCard;
    }

    public List<Rental> getCurrentRentedMovies() {
        return new ArrayList<Rental>(currentRentedMovies);
    }

    public int getMaxMovieRented() {
        return maxMovieRented;
    }

    public List<Rental> getHistory() {
        return new ArrayList<Rental>(history);
    }

    public List<String> getCategoryRestrained() {
        return new ArrayList<String>(categoryRestrained);
    }

    public List<String> getMoviesRestrained() {
        return new ArrayList<String>(moviesRestrained);
    }

    public UUID getSubscriberId() {
        return subscriberId;
    }

    public long getCreditCard () { return this.creditCard; }

    public void restrictChildMovieByTitle(ChildSubscriber c, String title) {
        System.out.println("Il faut avoir des enfants pour restreindre les films d'un enfant");
    }

    public void restrictChildMovieByCategory(ChildSubscriber c, String category) {
        System.out.println("Il faut avoir des enfants pour restreindre les films d'un enfant");
    }

    public void showChildHistory() {
        System.out.println("Il faut avoir des enfants pour afficher l'historique d'un enfant");
    }
}
