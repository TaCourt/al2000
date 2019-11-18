package src.core;

public class Movie {
    String affiche;
    String titre;
    String categorie;
    String synopsis;
    Integer duree;
    String langue;
    String acteurs;
    String realisateurs;
    String identifiant;

    public Movie(String affiche, String titre, String categorie, String synopsis, int duree, String langue, String acteurs, String realisateurs, String identifiant){
        this.affiche = affiche;
        this.titre = titre;
        this.categorie = categorie;
        this.synopsis = synopsis;
        this.duree = duree;
        this.langue = langue;
        this.acteurs = acteurs;
        this.realisateurs = realisateurs;
        this.identifiant = identifiant;
    }
}
