package src.gui;

import java.util.List;
import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {

     src.core.VideoClub videoClub;

     String prenom;
     String nom;
     Boolean isConnected;

    //Interface parent --> afficher historique enfant + restreindre cat enfant.
    // Interface enfant, pas d'option de restriction
    public ConsoleUserInterface(src.core.VideoClub videoClub){
        this.videoClub = videoClub;
        isConnected = false;
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
    public void cyberVideoMovies() {
        List<String[]> movies = videoClub.getCyberVideoMovie();
        printMovieList(movies);
        redirectToMainMenu();
    }

    @Override
    public void supposedAvailableMovies() {
        List<String[]> movies = videoClub.getAl2000Movies();
        printMovieList(movies);
        redirectToMainMenu();
    }

    public void availableMovies() {
        List<String[]> movies = videoClub.getAvailableMovies();
        printMovieList(movies);
        redirectToMainMenu();
    }

    @Override
    public void signUp() {
        System.out.println("Formulaire de création de compte :");
        String[] userData = new String[3];
        userData[0] = getStringFromUser("Nom :");
        userData[1] = getStringFromUser("Prénom:");
        userData[2] = getStringFromUser("Numéro de carte bleue:");

        String validationWord = printUserFormFinalValidation();

        if(validationWord.equals("valider")){
            videoClub.createNewSubscriber(userData);
            System.out.println("Opération validée.");
            System.out.println("Faites une demande de carte " +
                    "d'abonné pour vous connecter lors de votre prochaine visite");
        }else if( validationWord.equals("annuler")){
            System.out.println("Opération annulée.");
        }
        redirectToMainMenu();
    }

    @Override
    public void signUpForChild() {
        System.out.println("Formulaire de création de compte enfant :");
        String[] userData = new String[2];
        userData[0] = getStringFromUser("Nom :");
        userData[1] = getStringFromUser("Prénom:");

        String validationWord = printUserFormFinalValidation();

        if(validationWord.equals("valider")){
            videoClub.createNewChildSubscriber(userData);
            System.out.println("Opération validée.");
            System.out.println("Faites une demande de carte " +
                    "d'abonné pour permettre à votre enfant de se connecter lors de sa prochaine visite");
            System.out.println("Son compte sera rattaché au votre, et à votre carte bleue");
        }else if( validationWord.equals("annuler")){
            System.out.println("Opération annulée.");
        }
        redirectToMainMenu();
    }

    @Override
    public void signIn() {
        //TODO fixer les appels videoclub
        String numCarte = printSignInPage();
        String[] infosUser = videoClub.login(numCarte);
        redirectFromLoginPage(infosUser); // set isConnected, prenom et nom
    }



    @Override
    public void accountManagement() {
        printAccountManagement();
        List<String> categories = videoClub.getCategories();
        String chosenCategory = printCategoryList(categories);
        if (chosenCategory.isEmpty()){
            System.err.println("Erreur : entrez un nom de catégorie valide");
        }else{
            videoClub.restrictCategory(chosenCategory);
            System.out.println("Opération enregistré");
            System.out.println("la catégorie choisie n'apparaîtra plus dans les résultats, lors de vos prochaines recherches");
        }

        redirectToMainMenu();
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

        redirectToMainMenu();

    }

    @Override
    public void returnMovie() {
        String idMovieReturned = printReturnAMovie();
        videoClub.returnMovie(idMovieReturned);
        System.out.println("Veuillez placer le DVD dans la trappe");

        redirectToMainMenu();
    }

    @Override
    public void reportABug() {
        String report = printBugReportText();
        System.out.println("Merci pour votre participation.");
        redirectToMainMenu();
    }


    public void signOut(){ // TODO a finir coté videoclub après pull
        this.prenom = "";
        this.nom = "";
        this.isConnected = false;
        redirectToMainMenu();
    }

    public void welcomeSubscriberPage(String prenom, String nom){
        int userChoice = printWelcomeMenuSubscriber(prenom,nom);
        redirectFromMainSubscriberMenu(userChoice);
    }

    public void searchACategory(){ //TODO methode getMoviesOfCategory faite dans le prochain pull
        List<String> categories = videoClub.getCategories();
        String chosenCategory = printCategoryList(categories);
        if (chosenCategory.isEmpty()){
            System.err.println("Erreur : entrez un nom de catégorie valide");
        }else{
            List<String[]> moviesOfCategory = videoClub.getMoviesOfCategory(chosenCategory);
            printMovieList(moviesOfCategory);
        }

        redirectToMainMenu();
    }

    public void searchAMovie(){ //todo same
        String searchedTitle = printSearchFromTitle();
        String[] movie = videoClub.getMovieFromTitle(searchedTitle);
        if (movie == null) {
            System.err.println("Aucun film trouvé ayant ce titre, vérifiez l'orthograpge.");
        }else{
            System.out.println("Le film à été trouvé :");
            printMovie(movie);
        }

        redirectToMainMenu();
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
        System.out.println("-----------------------------------------------");
        System.out.println("-----  Bienvenue chez Cyber Vidéo !!  ---------");
        System.out.println("-----------------------------------------------");
        System.out.println();
        MenuHandler menu = new MenuHandler();
        menu.addOption("Vous avez un compte ? Connectez-vous.");
        menu.addOption("Lister les films disponible");
        menu.addOption("Lister tous les films de cet AL2000 (même ceux loués actuellement)");
        menu.addOption("Lister tous les films chez cyberVideo");
        menu.addOption("Rechercher un film par titre");
        menu.addOption("Afficher les films disponible d'une catégorie");
        menu.addOption("Louer un film");
        menu.addOption("Retourner un film");
        menu.addOption("Signaler un bug");
        menu.addOption("Créer un compte");
        return menu.getIntResponse();
    }

    private int printWelcomeMenuSubscriber(String prenom, String nom){
        System.out.println("--------------------------------------------------");
        System.out.println("------- Ravi de vous revoir " + prenom + " " + nom + " ! -------");
        System.out.println("--------------------------------------------------");
        System.out.println();
        return printSubscriberMainMenu();
    }

    private int printSubscriberMainMenu(){
        MenuHandler menu = new MenuHandler();
        menu.addOption("Lister les films disponible");
        menu.addOption("Lister tous les films de cet AL2000 (même ceux loués actuellement)");
        menu.addOption("Lister tous les films chez cyberVideo");
        menu.addOption("Rechercher un film par titre");
        menu.addOption("Afficher les films disponible d'une catégorie");
        menu.addOption("Louer un film");
        menu.addOption("Retourner un film");
        menu.addOption("Gérer mon compte");
        menu.addOption("Signaler un bug");
        menu.addOption("Créer un compte parrainé pour un enfant");
        menu.addOption("Déconnexion");

        return menu.getIntResponse();
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
        System.out.println("Pour louer un film, saisissez l'identifiant du film :");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String id = scanner.next();
        if( id == null ){
            return "";
        }else{
            return id;
        }
    }

    private void printRentMovieSuccess(){
        System.out.println("Traitement de la location en cours...");
        System.out.println("Vous pouvez récupérer votre film dans la trappe de sortie.");
        System.out.println();
    }

    private String printSearchFromTitle(){
        System.out.println("Recherche à partir d'un titre");
        System.out.println("Entrez le titre du film que vous cherchez :");
        Scanner scanner = new Scanner(System.in);
        String titre = scanner.next();
        if( titre == null ){
            return "";
        }else{
            return titre;
        }
    }

    private String printCategoryList(List<String> categoryList){
        StringBuilder categories = new StringBuilder();
        for( String categ : categoryList){
            categories.append(categ);
            categories.append(" | ");
        }
        System.out.println("Voici la liste des catégories disponible :");
        System.out.println(categories.toString());
        System.out.println();
        System.out.println("Entrez le nom de la catégorie à filtrer :");
        Scanner scanner = new Scanner(System.in);
        String chosenCategory = scanner.next();
        if( chosenCategory == null ){
            return "";
        }else{
            return chosenCategory;
        }
    }

    private String printBugReportText(){
        System.out.println("Merci de prendre le temps de nous signaler les problèmes rencontrés");
        System.out.println("Ceci nous aide grandement à faire évoluer notre système");
        System.out.println("Expliquez nous ce qu'il s'est passé en une ligne :");
        Scanner scanner = new Scanner(System.in);
        String report = scanner.next();
        if( report == null ){
            return "";
        }else{
            return report;
        }
    }

    private String printReturnAMovie(){
        System.err.println("Pour simuler l'acquisition du film par la machine, on fait remplir son id au client.");
        System.out.println("Pour rendre un film, veuillez composer son identifiant :");
        Scanner scanner = new Scanner(System.in);
        String idReturnedMovie = scanner.next();
        if( idReturnedMovie == null ){
            return "";
        }else{
            return idReturnedMovie;
        }
    }

    private void printAccountManagement(){
        System.out.println("Ici, vous pouvez limiter les films visible à votre compte");
        System.out.println("Choisissez la catégorie à limiter, parmis les suivantes :");
    }

    private String getStringFromUser(String text){
        System.out.println(text);
        Scanner scanner = new Scanner(System.in);
        String userEntry = scanner.next();
        if( userEntry == null ){
            userEntry = "";
        }
        while( userEntry == null || userEntry.isEmpty() ){
            System.err.println("Format invalide, entrez");
            System.out.println(text);
            userEntry = scanner.next();
        }
        return userEntry;
    }

    private String printUserFormFinalValidation(){
        System.out.println("Confirmer la création de compte ?");
        String validationWord = getStringFromUser(" Ecrivez \"valider\" ou \"annuler\"");
        while( !validationWord.equals("valider") && !validationWord.equals("annuler")){
            System.err.println("Réponse non reconnue");
            System.out.println("Confirmer la création de compte ?");
            validationWord = getStringFromUser(" Ecrivez \"valider\" ou \"annuler\"");
        }

        return validationWord;
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
                availableMovies();
                break;

            case 3:
                supposedAvailableMovies();
                break;

            case 4:
                cyberVideoMovies();
                break;

            case 5:
                searchAMovie();
                break;

            case 6:
                searchACategory();
                break;

            case 7:
                rentMovie();
                break;

            case 8:
                returnMovie();
                break;

            case 9:
                reportABug();
                break;

            case 10:
                signUp();
                break;
        }
    }

    private void redirectFromMainSubscriberMenu(int userChoice) {
        switch(userChoice){
            case 1:
                availableMovies();

            case 2:
                supposedAvailableMovies();
                break;

            case 3:
                cyberVideoMovies();
                break;

            case 4:
                searchAMovie();
                break;

            case 5:
                searchACategory();
                break;

            case 6:
                rentMovie();
                break;

            case 7:
                returnMovie();
                break;

            case 8:
                accountManagement();
                break;

            case 9:
                reportABug();
                break;

            case 10:
                signUpForChild();
                break;

            case 11:
                signOut();
                break;
        }
    }

    private void redirectFromLoginPage(String[] userInfos) {
        String prenom = userInfos[0];
        String nom = userInfos[1];
        if (!prenom.isEmpty() || !nom.isEmpty()) {
            isConnected = true;
            this.prenom = prenom;
            this.nom = nom;
            welcomeSubscriberPage(prenom,nom);
        } else {
            printLoginFailedMessage();
            welcomePage();
        }
    }

    private void redirectToMainMenu(){

        System.out.println("------------------------------------------");
        if(isConnected){
            int userChoice = printSubscriberMainMenu();
            redirectFromMainSubscriberMenu(userChoice);
        }else{
            int userChoice = printWelcomeMenu();
            redirectFromWelcomePage(userChoice);
        }

    }

}
