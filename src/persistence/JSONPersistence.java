package src.persistence;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class JSONPersistence implements Persistence {
    private final String filePath = new File("").getAbsolutePath();
    private final String SUBSCRIBER_DB = this.filePath.concat("/db/subscribers.json");
    private final String MOVIE_DB = this.filePath.concat("/db/movies.json");
    private final String AVAILABLE_MOVIE_DB = this.filePath.concat("/db/available_movies.json");
    private final String RENTAL_DB = this.filePath.concat("/db/rentals.json");

    private JSONObject SUBSCRIBER_JSON_DB;
    private JSONObject MOVIE_JSON_DB;
    private JSONObject AVAILABLE_MOVIE_JSON_DB;
    private JSONObject RENTAL_JSON_DB;

    /**
     * Initialise les variables correspondants aux base de données
     * @throws IOException s'il y a un problème de lecture des fichiers de base de données
     * @throws ParseException s'il y a un problème d'interprétation des fichiers de base de données (format corrompu)
     */
    public JSONPersistence () throws IOException, ParseException {
        this.SUBSCRIBER_JSON_DB = this.getJSONParser(this.SUBSCRIBER_DB);
        this.MOVIE_JSON_DB = this.getJSONParser(this.MOVIE_DB);
        this.AVAILABLE_MOVIE_JSON_DB = this.getJSONParser(this.AVAILABLE_MOVIE_DB);
        this.RENTAL_JSON_DB = this.getJSONParser(this.RENTAL_DB);
    }

    /**
     * Lis un fichier de base de donnée et le parse en JSONObject (map)
     * @param db chemin du fichier de base de données
     * @return l'objet JSONObject qui est la base de données associée au fichier de chemin db
     * @throws IOException s'il y a un problème de lecture des fichiers de base de données
     * @throws ParseException s'il y a un problème d'interprétation des fichiers de base de données (format corrompu)
     */
    private JSONObject getJSONParser (String db) throws IOException, ParseException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        FileReader reader = new FileReader(db);
        return (JSONObject) jsonParser.parse(reader);
    }

    /**
     * Sauvegarde la base de données JSON_db dans le fichier de chemin db
     * @param db chemin du fichier de base de données
     * @param JSON_db JSONObject qui est à la base de données
     */
    private void saveToFile (String db, JSONObject JSON_db) {
        try (FileWriter file = new FileWriter(db)) {
            file.write(JSON_db.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sauvegarde un utilisateur dans la base de données des abonnés
     * @param subscriberDetails map contenant les détails d'un abonné
     */
    @Override
    public void saveSubscriber(HashMap<String, String> subscriberDetails) {
        this.SUBSCRIBER_JSON_DB.put(subscriberDetails.get("UUID"), subscriberDetails);
        this.saveToFile(this.SUBSCRIBER_DB, this.SUBSCRIBER_JSON_DB);
    }

    /**
     * Sauvegarde un film dans la base de données des films
     * @param movieDetails map contenant les détails d'un film
     */
    @Override
    public void saveMovie(HashMap<String, String> movieDetails) {
        this.MOVIE_JSON_DB.put(movieDetails.get("id"), movieDetails);
        this.saveToFile(this.MOVIE_DB, this.MOVIE_JSON_DB);
    }

    /**
     * Ajoute/supprime un film dans la base de données des films disponibles
     * @param id id du film à sauvegarder
     * @param isAvailable indique si le film est disponible physiquement ou non
     * @param numberOfCopies nombre de copies restantes
     */
    @Override
    public void saveAvailableMovie (String id, boolean isAvailable, int numberOfCopies) {
        if (isAvailable == (this.AVAILABLE_MOVIE_JSON_DB.get(id) != null)) return;

        if (isAvailable) this.AVAILABLE_MOVIE_JSON_DB.put(id, numberOfCopies);
        else if (this.AVAILABLE_MOVIE_JSON_DB.get(id) != null) this.AVAILABLE_MOVIE_JSON_DB.remove(id);

        this.saveToFile(this.AVAILABLE_MOVIE_DB, this.AVAILABLE_MOVIE_JSON_DB);
    }

    /**
     * Charge tous les films disponibles physiquement
     * @return un tableau de string contenant les id des films disponibles physiquement
     */
    public HashMap<Long, Integer> getAvailableMovies () {
        HashMap<Long, Integer> availableMovies = new HashMap<Long, Integer>();
        this.AVAILABLE_MOVIE_JSON_DB.forEach((key, value) -> availableMovies.put(Long.parseLong((String) key), Integer.parseInt((String.valueOf(value)))));

        return availableMovies;
    }

    /**
     * Sauvegarde un location dans la base de données des locations
     * @param rentalDetails map contenant les détails d'une location
     */
    @Override
    public void saveRental(HashMap<String, String> rentalDetails) {
        this.RENTAL_JSON_DB.put(rentalDetails.get("UUID"), rentalDetails);
        this.saveToFile(this.RENTAL_DB, this.RENTAL_JSON_DB);
    }

    /**
     * Charge la location associée à un id
     * @param id id de l'abonné à charger
     * @return map contenant les détails de l'abonné qui a été chargé
     */
    public HashMap<String, String> loadSubscriber(String id) {
        return this.loadData(this.SUBSCRIBER_JSON_DB, id);
    }

    /**
     * Charge le film associé à un id
     * @param id id du film à charger
     * @return map contenant les détails du film qui a été chargé
     */
    public HashMap<String, String> loadMovie(String id) {
        return this.loadData(this.MOVIE_JSON_DB, id);
    }

    /**
     * Charge une location associée à un id
     * @param id id de la location à charger
     * @return map contenant les détails de la location qui a été chargée
     */
    public HashMap<String, String> loadRental(String id) {
        return this.loadData(this.RENTAL_JSON_DB, id);
    }

    /**
     * Charge l'item associé à un id dans la base de données db
     * @param db base de données
     * @param id id de l'item à charger
     * @return map de l'item qui a été chargé
     */
    private HashMap<String, String> loadData(JSONObject db, String id) {
        Object[] ids = db.keySet().toArray();

        for (Object _id : ids) {
            if (id.equals(_id)) {
                return (HashMap<String, String>) db.get(id);
            }
        }


        return null;
    }

    /**
     * Charge les ids de toutes les locations
     * @return Set des ids de toutes les locations
     */
    public Set<String> loadAllRentalIds () {
        return this.RENTAL_JSON_DB.keySet();
    }

    /**
     * Charge les ids de tous les films
     * @return Set des ids de tous les films
     */
    public Set<String> loadAllMoviesIds () {
        return this.MOVIE_JSON_DB.keySet();
    }

}
