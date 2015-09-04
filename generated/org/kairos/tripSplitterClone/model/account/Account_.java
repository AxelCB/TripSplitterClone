package org.kairos.tripSplitterClone.model.account;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.kairos.tripSplitterClone.model.account.Movement;

@Generated(value="EclipseLink-2.6.0.v20150309-rNA", date="2015-09-03T23:00:31")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, Boolean> deleted;
    public static volatile ListAttribute<Account, Movement> inMovements;
    public static volatile SingularAttribute<Account, BigDecimal> balance;
    public static volatile ListAttribute<Account, Movement> outMovements;
    public static volatile SingularAttribute<Account, Long> id;
    public static volatile SingularAttribute<Account, Date> creation;

}