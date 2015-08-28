package org.kairos.tripSplitterClone.model.account;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.kairos.tripSplitterClone.model.account.Account;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2015-08-28T00:34:25")
@StaticMetamodel(Movement.class)
public class Movement_ { 

    public static volatile SingularAttribute<Movement, Long> id;
    public static volatile SingularAttribute<Movement, BigDecimal> amount;
    public static volatile SingularAttribute<Movement, Date> timestamp;
    public static volatile SingularAttribute<Movement, Account> to;
    public static volatile SingularAttribute<Movement, Account> from;
    public static volatile SingularAttribute<Movement, Boolean> deleted;

}