package org.kairos.tripSplitterClone.model.trip;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.kairos.tripSplitterClone.model.destination.City;
import org.kairos.tripSplitterClone.model.trip.UserTrip;
import org.kairos.tripSplitterClone.model.user.User;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2015-08-31T01:06:13")
@StaticMetamodel(Trip.class)
public class Trip_ { 

    public static volatile SingularAttribute<Trip, User> owner;
    public static volatile SingularAttribute<Trip, Boolean> deleted;
    public static volatile SingularAttribute<Trip, City> destination;
    public static volatile SingularAttribute<Trip, Long> id;
    public static volatile ListAttribute<Trip, UserTrip> travelers;

}