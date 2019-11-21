package src.core;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Subscriber extends User {
    private UUID subscriberId;
    private String name;
    private String firstName;
    private double balanceSubscriberCard;
    private List<Rental> currentRentedMovies;
    private int maxMovieRented;

    private List<Rental> history;

    private List<String> categoryRestrained;
    private List<String> moviesRestrained;

    public Subscriber(UUID subscriberId, long creditCard, String name, String firstName, double balance) {
        super(creditCard);
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

    @Override
    public void rentingMovie(Movie m) {
        if (currentRentedMovies.size() != maxMovieRented) {
            addRental(m);
        } else {
            System.out.println("Tu as deja loué 3 films..");//A changer par une exception
        }
    }

    @Override
    public void returnMovie(Movie m) {
        for (Rental r : currentRentedMovies) {
            if (r.getMovie().getMovieId() == m.getMovieId()) {
                r.setReturnDate();
                history.add(r);
            }
        }
    }


    public void addRental(Movie m) {
        currentRentedMovies.add(new Rental(UUID.randomUUID(), m));
    }

    public void restrainMovieByTitle(String title) {
        moviesRestrained.add(title);
    }

    public void restrainMovieByCategory(String category) {
        categoryRestrained.add(category);
    }

    public boolean removeRestrainedMovie(String title) {
        return moviesRestrained.remove(title);
    }

    public boolean removeRestrainedCategory(String category) {
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

    public void restrainChildMovieByTitle(ChildSubscriber c, String title) {
        System.out.println("Il faut avoir des enfants pour restreindre les films d'un enfant");
    }

    public void restrainChildMovieByCategory(ChildSubscriber c, String category) {
        System.out.println("Il faut avoir des enfants pour restreindre les films d'un enfant");
    }

    public void showChildHistory() {
        System.out.println("Il faut avoir des enfants pour afficher l'historique d'un enfant");
    }
}
