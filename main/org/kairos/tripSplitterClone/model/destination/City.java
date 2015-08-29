package org.kairos.tripSplitterClone.model.destination;

import org.kairos.tripSplitterClone.model.I_Model;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
@Entity
@AutoProperty(policy= DefaultPojomaticPolicy.TO_STRING)
public class City implements Serializable,I_Model{

    /**
     * Entity ID.
     */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="city_seq")
    @SequenceGenerator(name="city_seq",sequenceName="city_seq",allocationSize=1)
    @Property(policy= PojomaticPolicy.HASHCODE_EQUALS)
    private Long id;

    /**
     * Logic deletion flag.
     */
    private Boolean deleted;

    /**
     * Name of the city
     */
    private String name;

    /**
     * City's country
     */
    @ManyToOne
    @Property(policy = PojomaticPolicy.NONE)
    private Country country;

    public City(){}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
