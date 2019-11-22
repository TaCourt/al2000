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
    public Rental (UUID rentalId, Movie movie, Date returnDate, Date rentingDate) {
        this.rentingDate = rentingDate;
        this.returnDate = returnDate;
        this.movie = movie;
        this.rentalId = rentalId;
    }
    public Rental ( Movie movie) {
        this(UUID.randomUUID(), movie);
    }
    public UUID getRentalId () {
        return this.rentalId;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public void setReturnDate () {
        this.returnDate = Date.from(Instant.now());
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public Date getRentingDate() {
        return rentingDate;
    }

}
