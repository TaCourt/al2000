package src.persistence;

import src.core.AdultSubscriber;
import src.core.ChildSubscriber;
import src.core.LambdaUser;

import java.util.HashMap;
import java.util.function.BiConsumer;

public interface Persistence {
    void saveSubscriber (HashMap<String, String> userDetails);
    void saveMovie (HashMap<String, String> movieDetails);
    void saveRental (HashMap<String, String> movieDetails);

    HashMap<String, String> loadSubscriber(String id);
    HashMap<String, String> loadMovie(String id);
    HashMap<String, String> loadRental(String id);

    void forEachSubscriber (BiConsumer<String, HashMap<String, String>> callback);

}
