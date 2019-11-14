package src.core;

import java.time.Instant;
import java.util.Date;

public class Rental {
    private Movie movie;
    private Date returnDate;
    private Date rentingDate;
    public Rental(Movie movie) {
        this.rentingDate = Date.from(Instant.now());
        this.movie = movie;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public void setReturnDate() {
        this.rentingDate = Date.from(Instant.now());
    }
}
