package src.core;

import java.util.ArrayList;
import java.util.List;

public class AdultSubscriber extends Subscriber {
    private List<ChildSubscriber> children;

    public AdultSubscriber(long creditCard, String name, String firstName, double balance) {
        super(creditCard, name, firstName, balance);
        this.children = new ArrayList<>();
    }

    public boolean addChild(ChildSubscriber child) {
        return children.add(child);
    }

    public boolean removeChild(ChildSubscriber child) {
        return children.remove(child);
    }

    public void restrainChildMovie(ChildSubscriber c, Movie m) {
        if (children.contains(c)) {
            c.restrainMovie(m);
        }
    }

    public void restrainChildMovie(ChildSubscriber c, String category) {
        if (children.contains(c)) {
            c.restrainMovie(category);
        }
    }

    public void showChildHistory() {

    }


}
