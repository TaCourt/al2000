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

    private JSONObject SUBSCRIBER_JSON_DB;
    private JSONObject MOVIE_JSON_DB;

    /*
     *************************
     * TODO ?
     *  Gérer les exceptions (de getJSONParser) pour faire remonter à l'user à travers l'IHM
     *  (Si ça échoue, aucun moyen d'utiliser la machine car pas de DB)
     */
    public JSONPersistence () throws FileNotFoundException, IOException, ParseException {
        this.SUBSCRIBER_JSON_DB = this.getJSONParser(this.SUBSCRIBER_DB);
        this.MOVIE_JSON_DB = this.getJSONParser(this.MOVIE_DB);
    }

    private JSONObject getJSONParser (String db) throws FileNotFoundException, IOException, ParseException {
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
        JSONObject JSONNUserDetails = new JSONObject(userDetails);

        this.SUBSCRIBER_JSON_DB.put(userDetails.get("UUID"), userDetails);
        this.saveToFile(this.SUBSCRIBER_DB, this.SUBSCRIBER_JSON_DB);
    }

    public HashMap<String, String> loadSubscriber(String id) {
        Object[] ids = this.SUBSCRIBER_JSON_DB.keySet().toArray();

        for (int i = 0; i < ids.length; i++) {
            if (id.equals(ids[i])) {
                return (HashMap<String, String>) this.SUBSCRIBER_JSON_DB.get(id);
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
