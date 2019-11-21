package src.core;

import java.util.UUID;

public class LambdaUserFactory extends UserFactory {
    @Override
    public User makeUser() {
        return new LambdaUser(0);
    }
}
