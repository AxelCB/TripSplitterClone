package org.kairos.tripSplitterClone.vo.destination;

import org.kairos.tripSplitterClone.vo.AbstractVo;

/**
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
public class DestinationVo extends AbstractVo{

    /**
     * Name of the city
     */
    private String cityName;

    /**
     * Name of the country
     */
    private String countryName;

    /**
     * Id of the city
     */
    private Long cityId;

    /**
     * Id of the country
     */
    private Long countryId;

    public DestinationVo(){}

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}
