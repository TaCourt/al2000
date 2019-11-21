package src.core;

import java.util.UUID;

public class AdultSubscriberFactory extends UserFactory {

    @Override
    public User makeUser() {
        return new AdultSubscriber(UUID.randomUUID(), 0, "", "", 0);
    }
    public User makeUser(String lastName, String firstName, String creditCard){
        return new AdultSubscriber(UUID.randomUUID(),Long.parseLong(creditCard),lastName,firstName,0);
    }
}
