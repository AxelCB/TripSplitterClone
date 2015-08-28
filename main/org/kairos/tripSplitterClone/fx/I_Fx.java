package org.kairos.tripSplitterClone.fx;

import org.kairos.tripSplitterClone.json.JsonResponse;
import org.kairos.tripSplitterClone.vo.AbstractVo;

import javax.persistence.EntityManager;

/**
 * General Interface for Fx's.
 * 
 * @author fgonzalez
 *
 */
public interface I_Fx {

	/**
	 * Executes the FX
	 * 
	 * @return a JsonResponse
	 */
	public JsonResponse execute();
	
	/**
	 * Sets the Entity Manager.
	 * 
	 * @param em
	 */
	public void setEm(EntityManager em);
	
	/**
	 * Returns the entity manager being hold by the FX.
	 * 
	 * @return entity manager
	 */
	public EntityManager getEm();
	
	/**
	 * Sets the Value Object.
	 * 
	 * @param vo
	 */
	public void setVo(AbstractVo vo);
	
}
