package src.core;

import java.util.List;

public class ChildSubscriber extends Subscriber {
    private List<AdultSubscriber> parents;

    public ChildSubscriber(long creditCard, String name, String firstName, double balance, AdultSubscriber parent) {
        super(creditCard, name, firstName, balance);
        parents.add(parent);
    }
}
