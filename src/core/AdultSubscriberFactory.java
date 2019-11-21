package src.core;

import java.util.UUID;

public class AdultSubscriberFactory extends SubscriberFactory {

    @Override
    public Subscriber makeUser() {
        return new AdultSubscriber(UUID.randomUUID(), 0, "", "", 0);
    }
    public Subscriber makeUser(String lastName, String firstName, String creditCard){
        return new AdultSubscriber(UUID.randomUUID(),Long.parseLong(creditCard),lastName,firstName,0);
    }
}
