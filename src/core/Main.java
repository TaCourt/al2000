package src.core;

import src.persistence.JSONPersistence;
import src.persistence.Persistence;

public class Main {
    public static void main (String[] args) {
        /*UserFactory uf = new LambdaUserFactory();
        LambdaUser lambdaUser = (LambdaUser) uf.makeUser();

        uf = new ChildSubscriberFactory();
        ChildSubscriber childSubscriber = (ChildSubscriber) uf.makeUser();;
        ChildSubscriber childSubscriber2 = (ChildSubscriber) uf.makeUser();

        uf = new AdultSubscriberFactory();
        AdultSubscriber adultSubscriber = (AdultSubscriber) uf.makeUser();
        adultSubscriber.addChild(childSubscriber);
        adultSubscriber.addChild(childSubscriber2);


        VideoClub v = new VideoClub();
        v.save(lambdaUser);
        v.save(childSubscriber);
        v.save(childSubscriber2);
        v.save(adultSubscriber);*/

        VideoClub v = new VideoClub();
        AdultSubscriber u = (AdultSubscriber) v.loadSubscriber("a6c21b11-a26f-4529-895d-8ff84f1398a7");
        System.out.println(u.getCreditCard());
        System.out.println(u.getChildren());
    }
}
