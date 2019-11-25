package src.core;

import java.util.UUID;

public class ChildSubscriberFactory extends SubscriberFactory {

    public Subscriber makeUser(String lastName, String firstName, AdultSubscriber adultSub){
        ChildSubscriber child = new ChildSubscriber(UUID.randomUUID(),adultSub.getCreditCard(),lastName,firstName,0,adultSub);
        adultSub.addChild(child);
        return child;
    }

    @Override
    public Subscriber makeUser() {
        return null;
    }
}
