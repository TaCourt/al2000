package src.core;

public class LambdaUser extends User {
    private Rental currentRental;

    public LambdaUser(long card) {
        super(card);
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
}