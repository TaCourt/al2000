package src.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Subscriber extends User {
    private String name;
    private String firstName;
    private double balanceSubscriberCard;
    private List<Rental> currentRentedMovies;
    private int maxMovieRented;

    private List<Rental> history;

    private List<String> categoryRestrained;
    private List<Movie> moviesRestrained;

    public Subscriber(long creditCard, String name, String firstName, double balance) {
        super(creditCard);
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
        currentRentedMovies.add(new Rental(m));
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
}
