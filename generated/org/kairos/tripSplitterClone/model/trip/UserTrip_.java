package org.kairos.tripSplitterClone.model.trip;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.kairos.tripSplitterClone.model.trip.Trip;
import org.kairos.tripSplitterClone.model.user.User;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2015-09-01T09:03:54")
@StaticMetamodel(UserTrip.class)
public class UserTrip_ { 

    public static volatile SingularAttribute<UserTrip, Boolean> deleted;
    public static volatile SingularAttribute<UserTrip, Trip> trip;
    public static volatile SingularAttribute<UserTrip, Long> id;
    public static volatile SingularAttribute<UserTrip, User> user;

}