package src.core;

public abstract class User {

    private long creditCard;

    public User(long card) {
        this.creditCard = card;
    }

    public long getCreditCard () { return this.creditCard; }

    public abstract void rentingMovie(Movie m);

    public abstract void returnMovie(Movie m);


}
