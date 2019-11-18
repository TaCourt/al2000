package src.persistence;

import src.core.AdultSubscriber;
import src.core.ChildSubscriber;
import src.core.LambdaUser;

import java.util.HashMap;

public interface Persistence {
    void saveUser (HashMap<String, String> userDetails);

    HashMap<String, String> loadUser (String id);
}
