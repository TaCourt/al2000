package src.core;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Subscriber extends User {
    private String name;
    private String firstName;
    private double balanceSubscriberCard;
    private List<Rental> currentRentedMovies;
    private int maxMovieRented;

    private List<Rental> history;

    private List<String> categoryRestrained;
    private List<Movie> moviesRestrained;

    public Subscriber(UUID subscriberId, long creditCard, String name, String firstName, double balance) {
        super(subscriberId, creditCard);
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
            if (r.getMovie().equals(m)) {
                r.setReturnDate();
            }
        }
    }


    public void addRental(Movie m) {
        currentRentedMovies.add(new Rental(UUID.randomUUID(), m));
    }

    public void restrainMovie(Movie name) {
        moviesRestrained.add(name);
    }

    public void restrainMovie(String category) {
        categoryRestrained.add(category);
    }

    public boolean removeRestrainedMovie(Movie name) {
        return moviesRestrained.remove(name);
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

    public List<Movie> getMoviesRestrained() {
        return new ArrayList<Movie>(moviesRestrained);

    }
}
