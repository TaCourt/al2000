package src.core;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Rental {
    private UUID rentalId;
    private Movie movie;
    private Date returnDate;
    private Date rentingDate;

    public Rental (UUID rentalId, Movie movie) {
        this.rentingDate = Date.from(Instant.now());
        this.movie = movie;
        this.rentalId = rentalId;
    }
    public UUID getRentalId () {
        return this.rentalId;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public void setReturnDate() {
        this.rentingDate = Date.from(Instant.now());
    }

}
