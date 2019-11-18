package src.core;

import src.core.ChildSubscriber;

import java.util.UUID;

public class ChildSubscriberFactory extends UserFactory {
    @Override
    public User makeUser() {
        return new ChildSubscriber(UUID.randomUUID(),0, "", "", 0, (AdultSubscriber) new AdultSubscriberFactory().makeUser());
    }
}
