package org.kairos.tripSplitterClone.vo.destination;

import org.apache.commons.lang3.StringUtils;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.web.I_MessageSolver;

/**
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
public class CityVo extends AbstractVo{

    /**
     * Name of the city
     */
    private String name;

    /**
     * City's country
     */
    private CountryVo country;

    /**
     * Default empty constructor
     */
    public CityVo(){}

    /**
     * Constructor with country
     */
    public CityVo(CountryVo country){
        this.country=country;
    }

    @Override
    public String validate(I_MessageSolver messageSolver) {
        if(StringUtils.isBlank(this.getName())){
            return messageSolver.getMessage("fx.city.field.name.notNull");
        }
        if(this.getCountry()==null){
            return messageSolver.getMessage("fx.city.field.country.notNull");
        }
        return super.validate(messageSolver);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryVo getCountry() {
        return country;
    }

    public void setCountry(CountryVo country) {
        this.country = country;
    }
}
