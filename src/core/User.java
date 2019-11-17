package src.core;

public abstract class User {
    private long creditCard;

    public User(long card) {
        creditCard = card;
    }

    public abstract void rentingMovie(Movie m);

    public abstract void returnMovie(Movie m);

    public void softawareError(){};

    public void movieError(){};


}
