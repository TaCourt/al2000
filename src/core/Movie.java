package src.core;


public class Movie{
    private Long movieId;
    private String affiche;
    private String title;
    private String category;
    private String synopsis;
    private Integer duration;
    private String language;
    private String actor;
    private String director;
    private boolean available;


    public Movie(Long movieId, String affiche, String titre, String categorie, String synopsis, int duree, String langue, String acteurs, String realisateurs, boolean available){
        this.movieId = movieId;
        this.affiche = affiche;
        this.title = titre;
        this.category = categorie;
        this.synopsis = synopsis;
        this.duration = duree;
        this.language = langue;
        this.actor = acteurs;
        this.director = realisateurs;
        this.available = available;
    }

    public Long getMovieId() {
        return this.movieId;
    }

    public String getAffiche() {
        return affiche;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getLanguage() {
        return language;
    }

    public String getActor() {
        return actor;
    }

    public String getDirector() {
        return director;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailability(boolean b) {
        this.available = b;
    }
}
