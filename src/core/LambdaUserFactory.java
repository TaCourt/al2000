package src.core;

public class LambdaUserFactory extends UserFactory {
    @Override
    public User makeUser() {
        return new LambdaUser();
    }
}
