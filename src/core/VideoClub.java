package src.core;

import java.util.Iterator;
import java.util.List;

public class VideoClub {


    private List<Technician> technicians;
    private UserFactory factory;
    private List<Subscriber> subscribers;
    private FilmLibrary movieLibrary;
    private User currentUser;
    private int fineCost;

    public VideoClub() {

    }

    public void addTechnicians(Technician technician) {
        this.technicians.add(technician);
    }

    public void addUsers(Subscriber user) {
        this.subscribers.add(user);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public int getFineCost() {
        return fineCost;
    }

    public void setFineCost(int fineCost) {
        this.fineCost = fineCost;
    }

    public void logIn(long card) {
        boolean subSuccess = false;
        Subscriber sub = null;
        Iterator<Subscriber> it = subscribers.iterator();
        while (it.hasNext() && !subSuccess) {
            sub = it.next();
            subSuccess = sub.identify(card);
        }
        if (sub != null) {
            currentUser = sub;
        } else {
            //exception
        }

    }

    public List<Movie> getCyberVideoMovie() {
        return movieLibrary.getCyberVideoMovies();
    }

    public List<Movie> getAvailableMovies() {
        return movieLibrary.getAvailableMovies();
    }

    public void rentingMovie(Movie m) {
        if (currentUser != null) {
            currentUser.rentingMovie(m);
        }
    }

    public void returnMovie(Movie m) {
        if (currentUser != null) {
            currentUser.returnMovie(m);
        }
    }

}
