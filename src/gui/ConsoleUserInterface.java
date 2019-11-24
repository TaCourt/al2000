package src.gui;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {

     private src.core.VideoClub videoClub;

     private String firstName;
     private String lastName;
     private Boolean isConnected;

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
        MenuHandler menu = new MenuHandler();
        int userChoice = printMenu(menu);
        redirect(menu.getKeyword(userChoice));
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

    private void availableMovies() {
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
            String id = videoClub.createNewSubscriber(userData);
            System.out.println("Opération validée.");
            System.out.println("Faites une demande de carte " +
                    "d'abonné pour vous connecter lors de votre prochaine visite");
            System.out.println("Voilà votre numéro d'identification :");
            System.out.println(id);
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
        String numCarte = printSignInPage();
        String[] infosUser = videoClub.logIn(numCarte);
        redirectFromLoginPage(infosUser); // set isConnected, firstName et lastName
    }



    @Override
    public void accountManagement() {
        printAccountManagement();
        List<String> categories = videoClub.getCategories();
        String chosenCategory = printCategoryList(categories);
        if (chosenCategory.isEmpty()){
            System.err.println("Erreur : entrez un lastName de catégorie valide");
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


    public void signOut(){
        this.firstName = "";
        this.lastName = "";
        this.isConnected = false;
        videoClub.logOut();
        redirectToMainMenu();
    }


    private void searchACategory(){
        List<String> categories = videoClub.getCategories();
        String chosenCategory = printCategoryList(categories);
        if (chosenCategory.isEmpty()){
            System.err.println("Erreur : entrez un lastName de catégorie valide");
        }else{
            List<String[]> moviesOfCategory = videoClub.getMoviesByCategory(chosenCategory);
            printMovieList(moviesOfCategory);
        }

        redirectToMainMenu();
    }

    private void searchAMovie(){
        String searchedTitle = printSearchFromTitle();
        List<String[]> movies = videoClub.getMovieByTitle(searchedTitle);
        if (movies == null) {
            System.err.println("Aucun film n'a été trouvé ayant ce titre, vérifiez l'orthograpge.");
        }else{
            printMovieList(movies);
        }

        redirectToMainMenu();
    }

    private void manageChildRestrictionCategory(){
        MenuHandler menu = new MenuHandler();
        Map<String,String> names = videoClub.getChildrenNames();
        int userChoice = printChildRestrictPage(menu,names);

        System.out.println("Maintenant, choisissez la catégorie à limiter.");
        List<String> categories = videoClub.getCategories();
        String chosenCategory = printCategoryList(categories);
        if (chosenCategory.isEmpty()){
            System.err.println("Erreur : entrez un lastName de catégorie valide");
        }else{
            videoClub.restrictCategoryForChild(menu.getKeyword(userChoice),chosenCategory);
            System.out.println("Opération enregistré");
            System.out.println("la catégorie choisie n'apparaîtra plus dans les résultats, lors de ses prochaines recherches");
        }

        redirectToMainMenu();
    }


    // ---------------------------------------------------------
    // Méthodes d'affichage et de récupérations d'informations
    // ---------------------------------------------------------


    private void printMenuHeader(String prenom, String nom){
        System.out.println("--------------------------------------------------");
        System.out.println("------- Ravi de vous revoir " + prenom + " " + nom + " ! -------");
        System.out.println("--------------------------------------------------");
        System.out.println();
    }

    private void printWelcomeHeader(){
        System.out.println("-----------------------------------------------");
        System.out.println("-----  Bienvenue chez Cyber Vidéo !!  ---------");
        System.out.println("-----------------------------------------------");
        System.out.println();
    }

    private int printMenu(MenuHandler menu){
        if( isConnected ){
            printMenuHeader(firstName, lastName);
        }else{
            printWelcomeHeader();
        }

        menu.addOption("Lister les films disponible","availableMovies");
        menu.addOption("Lister tous les films de cet AL2000 (même ceux loués actuellement)","supposedAvailableMovies");
        menu.addOption("Lister tous les films chez cyberVideo","cyberVideoMovies");
        menu.addOption("Rechercher un film par titre","searchByTitle");
        menu.addOption("Afficher les films disponible d'une catégorie","searchByCategory");
        menu.addOption("Louer un film","rentAMovie");
        menu.addOption("Retourner un film","returnAMovie");
        menu.addOption("Signaler un bug","reportABug");

        if( videoClub.canAccessChildAccountCreationPage())
            menu.addOption("Créer un compte parrainé pour un enfant","createChildAccount");

        if( videoClub.canAccessChildRestrictionPage() )
            menu.addOption("Gérer les restrictions de vos enfants","childRestrictions");

        if( isConnected ){
            menu.addOption("Gérer mon compte","accountManagement");
            menu.addOption("Déconnexion","logOut");
        }else{
            menu.addOption("Créer un compte","signUp");
            menu.addOption("Vous avez un compte ? Connectez-vous.","signIn");
        }

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
        System.out.println("Entrez le lastName de la catégorie à filtrer :");
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

    private String printSignInPage(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connexion :");
        System.out.println();
        System.out.println("Passez votre carte sur le lecteur, ou entrez son numéro ci-après :");
        return scanner.next();
    }

    private int printChildRestrictPage(MenuHandler menu, Map<String,String> names){
        System.out.println("Ici vous pouvez restreindre l'acces à certaines catégories, pour vos enfants. ");
        System.out.println("Pour commencer,choisissez à qui vous voulez appliquer ce filtre :");

        for(Map.Entry<String, String> entry : names.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();
            menu.addOption(valeur,cle);
        }

        return menu.getIntResponse();
    }



    // ---------------------------------------------------------
    // Méthodes de redirection en fonction du choix utilisateur.
    // ---------------------------------------------------------

    private void redirect(String userChoiceKeyWord){
        switch(userChoiceKeyWord){
            case "availableMovies":
                availableMovies();
                break;

            case "supposedAvailableMovies":
                supposedAvailableMovies();
                break;

            case "cyberVideoMovies":
                cyberVideoMovies();
                break;

            case "searchByTitle":
                searchAMovie();
                break;

            case "searchByCategory":
                searchACategory();
                break;

            case "rentAMovie":
                rentMovie();
                break;

            case "returnAMovie":
                returnMovie();
                break;

            case "reportABug":
                reportABug();
                break;

            case "createChildAccount":
                signUpForChild();
                break;

            case "childRestrictions":
                manageChildRestrictionCategory();
                break;

            case "accountManagement":
                accountManagement();
                break;

            case "logOut":
                signOut();
                break;

            case "signUp":
                signUp();
                break;

            case "signIn":
                signIn();
                break;

        }
    }

    private void redirectFromLoginPage(String[] userInfos) {
        String prenom = userInfos[0];
        String nom = userInfos[1];
        if (!prenom.isEmpty() || !nom.isEmpty()) {
            isConnected = true;
            this.firstName = prenom;
            this.lastName = nom;
        } else {
            printLoginFailedMessage();
        }
        redirectToMainMenu();
    }

    private void redirectToMainMenu(){
        System.out.println("------------------------------------------");
        welcomePage();
    }

}
