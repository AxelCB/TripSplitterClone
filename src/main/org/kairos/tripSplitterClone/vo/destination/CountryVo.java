package org.kairos.tripSplitterClone.vo.destination;

import org.kairos.tripSplitterClone.vo.AbstractVo;

/**
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
public class CountryVo extends AbstractVo{

    /**
     * Name of the city
     */
    private String name;

    public CountryVo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
