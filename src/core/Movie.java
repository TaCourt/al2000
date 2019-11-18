package src.core;


import java.util.UUID;

public class Movie implements Cloneable {
    private String affiche;
    private String titre;
    private String categorie;
    private String synopsis;
    private int duree;
    private String langue;
    //private ...... liste;
    private String acteurs;
    private String realisateurs;
    private UUID movieID;

    public Movie(String affiche, String titre, String categorie, String synopsis, int duree, String langue, String acteurs, String realisateurs){
        this.affiche = affiche;
        this.titre = titre;
        this.categorie = categorie;
        this.synopsis = synopsis;
        this.duree = duree;
        this.langue = langue;
        this.acteurs = acteurs;
        this.realisateurs = realisateurs;
        this.movieID = UUID.randomUUID();
    }

    public UUID getMovieID() {
        return this.movieID;
    }
}