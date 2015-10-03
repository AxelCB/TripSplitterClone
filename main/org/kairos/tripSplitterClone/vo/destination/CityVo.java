package org.kairos.tripSplitterClone.vo.destination;

import org.apache.commons.lang3.StringUtils;
import org.kairos.tripSplitterClone.utils.exception.ValidationException;
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

    public CityVo(String name, CountryVo country) throws ValidationException {
        String validationResponse = country.validate();
        if(validationResponse != null){
            throw new ValidationException(validationResponse);
        }
        this.name = name;
        this.country = country;
    }

    @Override
    public String validate() {
        if(StringUtils.isBlank(this.getName())){
            return "City name cannot be blank";
        }
        if(this.getCountry()==null){
            return "City needs a country";
        }
        if(this.getCountry().validate()!=null){
            return this.getCountry().validate();
        }
        return null;
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
