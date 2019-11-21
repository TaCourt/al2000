package src.persistence;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class JSONPersistence implements Persistence {
    private static JSONPersistence instance = null;

    private final String filePath = new File("").getAbsolutePath();
    private final String SUBSCRIBER_DB = this.filePath.concat("/db/subscribers.json");
    private final String MOVIE_DB = this.filePath.concat("/db/movies.json");
    private final String AVAILABLE_MOVIE_DB = this.filePath.concat("/db/available_movies.json");
    private final String RENTAL_DB = this.filePath.concat("/db/rentals.json");

    private JSONObject SUBSCRIBER_JSON_DB;
    private JSONObject MOVIE_JSON_DB;
    private JSONObject AVAILABLE_MOVIE_JSON_DB;
    private JSONObject RENTAL_JSON_DB;

    public JSONPersistence () throws IOException, ParseException {
        this.SUBSCRIBER_JSON_DB = this.getJSONParser(this.SUBSCRIBER_DB);
        this.MOVIE_JSON_DB = this.getJSONParser(this.MOVIE_DB);
        this.AVAILABLE_MOVIE_JSON_DB = this.getJSONParser(this.AVAILABLE_MOVIE_DB);
        this.RENTAL_JSON_DB = this.getJSONParser(this.RENTAL_DB);
    }

    private JSONObject getJSONParser (String db) throws IOException, ParseException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        FileReader reader = new FileReader(db);
        JSONObject list = (JSONObject) jsonParser.parse(reader);
        return list;
    }

    private void saveToFile (String db, JSONObject JSON_db) {
        try (FileWriter file = new FileWriter(db)) {
            file.write(JSON_db.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveSubscriber(HashMap<String, String> userDetails) {
        this.SUBSCRIBER_JSON_DB.put(userDetails.get("UUID"), userDetails);
        this.saveToFile(this.SUBSCRIBER_DB, this.SUBSCRIBER_JSON_DB);
    }

    @Override
    public void saveMovie(HashMap<String, String> movieDetails) {
        this.MOVIE_JSON_DB.put(movieDetails.get("id"), movieDetails);
        this.saveToFile(this.MOVIE_DB, this.MOVIE_JSON_DB);
    }

    @Override
    public void saveAvailableMovie (String id, boolean isAvailable) {
        // not XOR
        if (!(isAvailable ^ this.AVAILABLE_MOVIE_JSON_DB.get(id) != null)) return;

        if (isAvailable) this.AVAILABLE_MOVIE_JSON_DB.put(id, "");
        else if (this.AVAILABLE_MOVIE_JSON_DB.get("id") != null) this.AVAILABLE_MOVIE_JSON_DB.remove(id);

        this.saveToFile(this.AVAILABLE_MOVIE_DB, this.AVAILABLE_MOVIE_JSON_DB);
    };

    public String[] getAvailableMovies () {
        /*String[] availableMovies = new String[this.AVAILABLE_MOVIE_JSON_DB.keySet().size()];

        for (int i = 0; i < this.AVAILABLE_MOVIE_JSON_DB.keySet().size(); i++) {
            availableMovies[i] = (String) this.AVAILABLE_MOVIE_JSON_DB.get(Integer.toString(i));
        }

        return availableMovies;*/

        // TODO
        return null;
    }

    @Override
    public void saveRental(HashMap<String, String> rentalDetails) {
        this.RENTAL_JSON_DB.put(rentalDetails.get("id"), rentalDetails);
        this.saveToFile(this.RENTAL_DB, this.RENTAL_JSON_DB);
    }

    public HashMap<String, String> loadSubscriber(String id) {
        return this.loadData(this.SUBSCRIBER_JSON_DB, id);
    }

    public HashMap<String, String> loadMovie(String id) {
        return this.loadData(this.MOVIE_JSON_DB, id);
    }

    public HashMap<String, String> loadRental(String id) {
        return this.loadData(this.RENTAL_JSON_DB, id);
    }

    private HashMap<String, String> loadData(JSONObject db, String id) {
        Object[] ids = db.keySet().toArray();

        for (int i = 0; i < ids.length; i++) {
            if (id.equals(ids[i])) {
                return (HashMap<String, String>) db.get(id);
            }
        }

        return null;
    }

    public void forEachSubscriber(BiConsumer<String, HashMap<String, String>> callback) {
        this.SUBSCRIBER_JSON_DB.forEach((id, userDetails) -> {
            callback.accept((String) id, (HashMap<String, String>) userDetails);
        });
    }

}
