package src.persistence;

import src.core.AdultSubscriber;
import src.core.ChildSubscriber;
import src.core.LambdaUser;

import java.util.HashMap;
import java.util.function.BiConsumer;

public interface Persistence {
    void saveSubscriber(HashMap<String, String> userDetails);

    HashMap<String, String> loadSubscriber(String id);

    void forEachSubscriber(BiConsumer<String, HashMap<String, String>> callback);
}
