package src.gui;

import java.util.List;
import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {

     src.core.VideoClub videoClub;


    public ConsoleUserInterface(src.core.VideoClub videoClub){
        this.videoClub = videoClub;
    }

    // ---------------------------------------------------------
    // Méthodes de déplacement
    // ---------------------------------------------------------

    @Override
    public void welcomePage() {
        int userChoice = printWelcomeMenu();
        redirectFromWelcomePage(userChoice);
    }

    @Override
    public void cyberVideoFilms() {

    }

    @Override
    public void supposedAvailableFilms() {
        List<String[]> movies = videoClub.getAvailableMovies();
        printMovieList(movies);
        int userChoice = printSubscriberMainMenu();
        redirectFromMainSubscriberMenu(userChoice);


    }

    @Override
    public void signUp() {

    }

    @Override
    public void signUpForChild() {

    }

    @Override
    public void signIn() {
        String numCarte = printSignInPage();
        String[] infosUser = videoClub.login(numCarte);
        redirectFromLoginPage(infosUser);
    }



    @Override
    public void accountManagement() {

    }

    @Override
    public void rentMovie() {
        String id = printRentMovie();
        if( videoClub.exists(id)){
            videoClub.rentMovie(id);
            printRentMovieSuccess();
        }else{
            System.err.println("Erreur : Identifiant de film introuuvable.");
            System.err.println("Utilisez les fonctions de recherche pour le trouver.");
        }

        printSubscriberMainMenu();

    }

    @Override
    public void returnMovie() {

    }

    @Override
    public void reportABug() {

    }

    public void searchAMovie(){

    }

    public void signOut(){

    }

    public void welcomeSubscriberPage(String prenom, String nom){
        int userChoice = printWelcomeMenuSubscriber(prenom,nom);
        redirectFromMainSubscriberMenu(userChoice);
    }

    // ---------------------------------------------------------
    // Méthodes d'affichage et de récupérations d'informations
    // ---------------------------------------------------------


    private String printSignInPage(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connexion :");
        System.out.println();
        System.out.println("Passez votre carte sur le lecteur, ou entrez son numéro ci-après :");
        String numCarte = scanner.next();

        return numCarte;
    }

    private int printWelcomeMenu(){
        System.out.println("Bienvenue chez Cyber Vidéo !!");
        System.out.println();
        MenuHandler menu = new MenuHandler();
        menu.addOption("Vous avez un compte ? Connectez-vous.");
        menu.addOption("Lister les films disponible");
        menu.addOption("Rechercher un film");
        menu.addOption("Louer un film");
        menu.addOption("Retourner un film");
        menu.addOption("Signaler un bug");
        int userChoice = menu.getIntResponse();
        return userChoice;
    }

    private int printWelcomeMenuSubscriber(String prenom, String nom){
        System.out.println("Ravi de vous revoir " + prenom + " " + nom + " !");
        System.out.println();
        int userChoice = printSubscriberMainMenu();
        return userChoice;
    }

    private int printSubscriberMainMenu(){
        MenuHandler menu = new MenuHandler();
        menu.addOption("Lister les films disponible");
        menu.addOption("Rechercher un film");
        menu.addOption("Louer un film");
        menu.addOption("Retourner un film");
        menu.addOption("Gérer mon compte");
        menu.addOption("Signaler un bug");
        menu.addOption("Déconnexion");

        int userChoice = menu.getIntResponse();
        return userChoice;
    }

    private void printLoginFailedMessage() {
        System.err.println("Erreur : Numéro de carte inconnu !");
    }


    private void printMovie(String[] movie){
        System.out.println("Affiche : " + movie[0] );
        System.out.println("Titre : " + movie[1] );
        System.out.println("Catégorie : " + movie[2] );
        System.out.println("Synopsis : " + movie[3] );
        System.out.println("Durée : " + movie[4] );
        System.out.println("Langue : " + movie[5] );
        System.out.println("Acteurs : " + movie[6] );
        System.out.println("Réalisateur : " + movie[7] );
        System.out.println("Identifiant : " + movie[8]);
    }
    private void printMovieList(List<String[]> movies){

        for( String[] movie : movies){
            printMovie(movie);
            System.out.println("---------------------------------------------------------");
            System.out.println();
        }
    }

    private String printRentMovie(){
        boolean isSuccess;
        System.out.println("Pour louer un film, saisissez l'identifiant du film :");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String id = scanner.next();
        return id;
    }

    private void printRentMovieSuccess(){
        System.out.println("Traitement de la location en cours...");
        System.out.println("Vous pouvez récupérer votre film dans la trappe de sortie.");
    }


    // ---------------------------------------------------------
    // Méthodes de redirection en fonction du choix utilisateur.
    // ---------------------------------------------------------

    private void redirectFromWelcomePage(int userChoice){
        switch(userChoice){
            case 1:
                signIn();
                break;

            case 2:
                supposedAvailableFilms();
                break;

            case 3:
                searchAMovie();
                break;

            case 4:
                rentMovie();
                break;

            case 5:
                returnMovie();
                break;

            case 6:
                reportABug();
                break;
        }
    }

    private void redirectFromMainSubscriberMenu(int userChoice) {
        switch(userChoice){
            case 1:
                supposedAvailableFilms();
                break;

            case 2:
                searchAMovie();
                break;

            case 3:
                rentMovie();
                break;

            case 4:
                returnMovie();
                break;

            case 5:
                accountManagement();
                break;

            case 6:
                reportABug();
                break;

            case 7:
                signOut();
                break;
        }
    }

    private void redirectFromLoginPage(String[] userInfos) {
        String prenom = userInfos[0];
        String nom = userInfos[1];
        if (!prenom.isEmpty() || !nom.isEmpty()) {
            welcomeSubscriberPage(prenom,nom);
        } else {
            printLoginFailedMessage();
            welcomePage();
        }
    }




}
