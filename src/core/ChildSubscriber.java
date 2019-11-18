package src.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChildSubscriber extends Subscriber {
    private AdultSubscriber parent;

    public ChildSubscriber(UUID childSubscriberId, long creditCard, String name, String firstName, double balance, AdultSubscriber parent) {
        super(childSubscriberId, creditCard, name, firstName, balance);
        this.parent = parent;
    }

    public AdultSubscriber getParent () {
        return this.parent;
    }
}
