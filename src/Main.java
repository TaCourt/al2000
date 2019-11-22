package src;

import org.json.simple.parser.ParseException;
import src.core.*;
import src.persistence.JSONPersistence;

import java.io.IOException;

class Main {
    public static void main (String[] args){
        VideoClub videoClub = new VideoClub();
        videoClub.launch();

        try {
            videoClub.setPersistence(new JSONPersistence());
            saveMovie(videoClub);
            System.out.println(videoClub.getDao().loadAvailableMovies());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void testLoadParentUser (VideoClub v) {
        v.logIn("a6c21b11-a26f-4529-895d-8ff84f1398a7");
        AdultSubscriber u = (AdultSubscriber) v.getCurrentSubscriber();
        System.out.println("Children : " + u.getChildren());
        System.out.println("name : " + u.getName());
        System.out.println("firstName : " + u.getFirstName());
        System.out.println("creditCard : " + Long.toString(u.getCreditCard()));
        System.out.println("balanceSubscriberCard : " + Double.toString(u.getBalanceSubscriberCard()));
        System.out.println("maxMovieRented : " + Integer.toString(u.getMaxMovieRented()));
        System.out.println("UUID : " + u.getSubscriberId().toString());
    }

    public static void saveMovie (VideoClub v) throws IOException, ParseException {
        VideoClubDAO dao = v.getDao();

        dao.saveMovie(new Movie((long) 0, "jsp ckec", "Le Projet", "COO", "ça change souvent",
                190, "Français", "MG, FC, TC, LB", "Les mêmes", false));

        dao.saveMovie(new Movie((long) 1, "un autre", "Le Projet - La suite", "COO", "ça change souvent",
                160, "Français", "MG, FC, TC, LB", "Les mêmes", true));


        dao.saveMovie(new Movie((long) 2, "jsp ckec", "Le Projet - La suite 2", "COO", "ça change souvent",
                120, "Anglais", "MG, FC, TC, LB", "Les mêmes", false));
    }
}
