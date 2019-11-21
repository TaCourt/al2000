package src.core;

import java.util.UUID;

public class ChildSubscriberFactory extends SubscriberFactory {

    public Subscriber makeUser(String lastName, String firstName, AdultSubscriber adultSub){
        return new ChildSubscriber(UUID.randomUUID(),adultSub.getCreditCard(),lastName,firstName,0,adultSub);
    }

    @Override
    public Subscriber makeUser() {
        return null;
    }
}
