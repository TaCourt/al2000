package src.core;

import src.core.ChildSubscriber;

public class ChildSubscriberFactory extends UserFactory {
    @Override
    public User makeUser() {
        return new ChildSubscriber();
    }
}
