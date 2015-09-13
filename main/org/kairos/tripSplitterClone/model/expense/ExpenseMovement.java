package org.kairos.tripSplitterClone.model.expense;

import org.kairos.tripSplitterClone.model.I_Model;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created on 9/10/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class ExpenseMovement implements Serializable,I_Model {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="expensemovement_seq")
	@SequenceGenerator(name="expensemovement_seq",sequenceName="expensemovement_seq",allocationSize=1)
	@Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
	private Long id;

	/**
	 * Logic deletion flag.
	 */
	private Boolean deleted = Boolean.FALSE;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Boolean getDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
