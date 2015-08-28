package org.kairos.tripSplitterClone.model.user;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.kairos.tripSplitterClone.model.trip.UserTrip;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2015-08-28T00:34:25")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, Long> hashCost;
    public static volatile ListAttribute<User, UserTrip> trips;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Boolean> deleted;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Integer> loginAttempts;

}