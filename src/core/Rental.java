package src.core;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Rental {
    public static final double NB_MS_IN_A_DAY = 8640000;
    private Double pricePerDay;
    private UUID rentalId;
    private Movie movie;
    private Date returnDate;
    private Date rentingDate;

    public Rental (UUID rentalId, Movie movie, Double pricePerDay) {
        this.rentingDate = Date.from(Instant.now());
        this.movie = movie;
        this.rentalId = rentalId;
        this.pricePerDay = pricePerDay;
    }
    public Rental ( Movie movie) {
        this(UUID.randomUUID(), movie, 5.0);
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

    /**
     * Calcule le prix correspondant à la location
     * @return ce prix
     */
    public Double getPrice() {
        long time = 0;
        if(returnDate.after(rentingDate)) {
            time = returnDate.getTime() - rentingDate.getTime();
        }
        return ((time / NB_MS_IN_A_DAY)+1) * pricePerDay;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

}
