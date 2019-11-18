package src.core;

import java.util.UUID;

public class AdultSubscriberFactory extends UserFactory {

    @Override
    public User makeUser() {
        return new AdultSubscriber(UUID.randomUUID(), 0, "", "", 0);
    }
}
