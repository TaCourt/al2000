package src.core;


public class Movie{
    private String affiche;
    private String titre;
    private String categorie;
    private String synopsis;
    private Int duree;
    private String langue;
    //private ...... liste;
    private String acteurs;
    private String realisateurs;

    //Constructeur
    public Movie(String affiche, String titre, String categorie, String synopsis, Int duree, String langue, String acteurs, String realisateurs){
        this.affiche = affiche;
        this.titre = titre;
        this.categorie = categorie;
        this.synopsis = synopsis;
        this.duree = duree;
        this.langue = langue;
        this.acteurs = acteurs;
        this.realisateurs = realisateurs;
    }
}