package src.core;

import java.util.UUID;

public class LambdaUser extends User {
    private Rental currentRental;

    public LambdaUser(UUID userId, long card) {
        super(userId, card);
    }

    @Override
    public void rentingMovie(Movie m) {
        if (currentRental == null) {
            System.out.println("Erreur : vous avez deja un film loué");
        } else {
            currentRental = new Rental(m);
        }
    }

    @Override
    public void returnMovie(Movie m) {
        currentRental = null;
    }

    public Rental getCurrentRental () {
        return this.currentRental;
    }
}