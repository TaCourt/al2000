package src.core;

public class AdultSubscriberFactory extends UserFactory {

    @Override
    public User makeUser() {
        return new AdultSubscriber();
    }
}
