package src.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VideoClub {

    Map<String,Subscriber> users;
    List<Movie> movieList;

    public VideoClub(){
        users = new HashMap<>();
        movieList = new LinkedList<>();
        Movie mov1 = new Movie("../afficher/pulpFiction.jpg","Pulp Fiction","Cool","Ya un pelo avec une affro et un gun",2,"Amerlock","Samuel L. Jackson","jesaispasgros","A234");
        Movie mov2 = new Movie("../afficher/timeOut.jpg","Time out","Nice","Le temps c'est de l'agrent.",2,"Amerlock","Mpockora mdr","jesaispasnonplusgros","V2345");
        movieList.add(mov1);
        movieList.add(mov2);
        Subscriber tanguy = new AdultSubscriber();
        tanguy.nom = "Court";
        tanguy.prenom = "Tanguy";
        users.put("123456",tanguy);
    }

    public String[] login(String numCarte){
        Subscriber user = users.get(numCarte);
        String[] infosUser = new String[2];

        if( user == null){
            infosUser[0] = "";
            infosUser[1] = "";
        }else{
            infosUser[0] = user.prenom;
            infosUser[1] = user.nom;
        }
        return infosUser;
    }

    public List<String[]> getAvailableMovies(){
        List<String[]> movieListData = new LinkedList<>();
        for( Movie movie : movieList){
            String [] movieData = new String[9];
            movieData[0] = movie.affiche;
            movieData[1] = movie.titre;
            movieData[2] = movie.categorie;
            movieData[3] = movie.synopsis;
            movieData[4] = movie.duree.toString();
            movieData[5] = movie.langue;
            movieData[6] = movie.acteurs;
            movieData[7] = movie.realisateurs;
            movieData[8] = movie.identifiant;
            movieListData.add(movieData);
        }
        return movieListData;
    }

    public boolean exists(String identifiant){
        return true;
    }

    public String[] getMovieFromTitle(String title){
        Movie movie = movieList.get(0); // recherche de la movie
        String [] movieData = new String[9];
        movieData[0] = movie.affiche;
        movieData[1] = movie.titre;
        movieData[2] = movie.categorie;
        movieData[3] = movie.synopsis;
        movieData[4] = movie.duree.toString();
        movieData[5] = movie.langue;
        movieData[6] = movie.acteurs;
        movieData[7] = movie.realisateurs;
        movieData[8] = movie.identifiant;
        return movieData;
    }
    public void rentMovie(String identifiant){

    }

    public List<String> getCategories(){
        List<String> categories = new LinkedList<>();
        categories.add("Horreur");
        categories.add("Fantastique");
        categories.add("Comédie");
        categories.add("Drame");
        categories.add("Pour Adulte");
        return categories;
    }

    public List<String[]> getMoviesOfCategory(String category){
        List<String[]> movieListData = new LinkedList<>();
        for( Movie movie : movieList){
            String [] movieData = new String[9];
            movieData[0] = movie.affiche;
            movieData[1] = movie.titre;
            movieData[2] = movie.categorie;
            movieData[3] = movie.synopsis;
            movieData[4] = movie.duree.toString();
            movieData[5] = movie.langue;
            movieData[6] = movie.acteurs;
            movieData[7] = movie.realisateurs;
            movieData[8] = movie.identifiant;
            movieListData.add(movieData);
        }
        return movieListData;
    }

    public void retunMovie(String idMovieReturned) {

    }

    public void restrictCategory(String chosenCategory) {

    }

    public void createNewSubscriber(String[] userData) {
//        userData[0] = nom
//        userData[1] = prenom
//        userData[2] = num CB
    }

    public void createNewChildSubscriber(String[] userData) {
//        userData[0] = nom
//        userData[1] = prenom
    }
}
