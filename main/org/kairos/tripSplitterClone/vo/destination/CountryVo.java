package org.kairos.tripSplitterClone.vo.destination;

import org.apache.commons.lang3.StringUtils;
import org.kairos.tripSplitterClone.vo.AbstractVo;
import org.kairos.tripSplitterClone.web.I_MessageSolver;

/**
 * Created on 8/23/15 by
 *
 * @author AxelCollardBovy.
 */
public class CountryVo extends AbstractVo{

    /**
     * Name of the country
     */
    private String name;

    public CountryVo(){}

    @Override
    public String validate(I_MessageSolver messageSolver) {
        if(StringUtils.isBlank(this.getName())){
            return messageSolver.getMessage("country.field.name.notNull");
        }
        return super.validate(messageSolver);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
