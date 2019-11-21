package src.core;

import src.core.ChildSubscriber;

import java.util.UUID;

public class ChildSubscriberFactory extends UserFactory {

    public User makeUser(String lastName, String firstName, AdultSubscriber adultSub){
        return new ChildSubscriber(UUID.randomUUID(),adultSub.getCreditCard(),lastName,firstName,0,adultSub);
    }

    @Override
    public User makeUser() {
        return null;
    }
}
