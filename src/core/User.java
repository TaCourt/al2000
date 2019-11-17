package src.core;

import java.util.UUID;

public abstract class User {
    private UUID userId;
    private long creditCard;

    public User(UUID userId, long card) {
        this.userId = userId;
        this.creditCard = card;
    }

    public UUID getUserId () {
        return this.userId;
    }
    public long getCreditCard () { return this.creditCard; }

    public abstract void rentingMovie(Movie m);

    public abstract void returnMovie(Movie m);

    public void softawareError(){};

    public void movieError(){};
}
