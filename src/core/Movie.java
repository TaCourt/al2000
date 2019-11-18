package src.core;


import java.util.UUID;


public class Movie{
    private UUID movieId;
    private String affiche;
    private String titre;
    private String categorie;
    private String synopsis;
    private int duree;
    private String langue;
    private String acteurs;
    private String realisateurs;

    public Movie(UUID movieId, String affiche, String titre, String categorie, String synopsis, int duree, String langue, String acteurs, String realisateurs){
        this.movieId = movieId;
        this.affiche = affiche;
        this.titre = titre;
        this.categorie = categorie;
        this.synopsis = synopsis;
        this.duree = duree;
        this.langue = langue;
        this.acteurs = acteurs;
        this.realisateurs = realisateurs;
    }

    public UUID getMovieId() {
        return this.movieId;
    }
}