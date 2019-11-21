package src.core;

import java.util.*;

public class AdultSubscriber extends Subscriber {

    private List<ChildSubscriber> children;

    public AdultSubscriber(UUID adultSubscriberId, long creditCard, String name, String firstName, double balance) {
        super(adultSubscriberId, creditCard, name, firstName, balance);
        this.children = new ArrayList<>();
    }

    public boolean addChild(ChildSubscriber child) {
        return children.add(child);
    }

    public boolean removeChild(ChildSubscriber child) {
        return children.remove(child);
    }

    @Override
    public void restrainChildMovieByTitle(ChildSubscriber c, String title) {
        if (children.contains(c)) {
            c.restrainMovieByTitle(title);
        }
    }

    @Override
    public void restrainChildMovieByCategory (ChildSubscriber c,String category) {
        if (children.contains(c)) {
            c.restrainMovieByCategory(category);
        }
    }

    @Override
    public void showChildHistory() {

    }

    public List<ChildSubscriber> getChildren() {
        return new ArrayList<ChildSubscriber>(this.children);
    }
}
