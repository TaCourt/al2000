package src.persistence;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import src.core.AdultSubscriber;
import src.core.ChildSubscriber;
import src.core.LambdaUser;
import src.core.Rental;

import java.io.*;
import java.util.HashMap;

public class JSONPersistence implements Persistence {
    private static JSONPersistence instance = null;

    private final String filePath = new File("").getAbsolutePath();
    private final String USER_DB = this.filePath.concat("/db/users.json");
    private final String MOVIE_DB = this.filePath.concat("/db/movies.json");

    private JSONObject USER_JSON_DB;
    private JSONObject MOVIE_JSON_DB;

    /*
     *************************
     * TODO ?
     *  Gérer les exceptions (de getJSONParser) pour faire remonter à l'user à travers l'IHM
     *  (Si ça échoue, aucun moyen d'utiliser la machine car pas de DB)
     */
    private JSONPersistence () {
        this.USER_JSON_DB = this.getJSONParser(this.USER_DB);
        this.MOVIE_JSON_DB = this.getJSONParser(this.MOVIE_DB);
    }

    private JSONObject getJSONParser (String db) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(db)) {
            JSONObject userList = (JSONObject) jsonParser.parse(reader);
            return userList;
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONPersistence getInstance () {
        if (instance == null) instance = new JSONPersistence();
        return instance;
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
    public void saveUser (HashMap<String, String> userDetails) {
        JSONObject JSONNUserDetails = new JSONObject(userDetails);

        this.USER_JSON_DB.put(userDetails.get("UUID"), userDetails);
        this.saveToFile(this.USER_DB, this.USER_JSON_DB);
    }

    public HashMap<String, String> loadUser (String id) {
        Object[] ids = this.USER_JSON_DB.keySet().toArray();

        for (int i = 0; i < ids.length; i++) {
            if (id.equals(ids[i])) {
                return (HashMap<String, String>) this.USER_JSON_DB.get(id);
            }
        }

        return null;
    }


}
