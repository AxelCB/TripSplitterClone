package org.kairos.tripSplitterClone.dao.destination;

import org.kairos.tripSplitterClone.dao.AbstractDao;
import org.kairos.tripSplitterClone.model.destination.City;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created on 8/29/15 by
 *
 * @author AxelCollardBovy.
 */
public class CityDaoJPAImpl extends AbstractDao<City, CityVo> implements I_CityDao {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(CityDaoJPAImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getClazz()
	 */
	@Override
	protected Class<City> getClazz() {
		return City.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universe.core.dao.AbstractDao#getVoClazz()
	 */
	@Override
	public Class<CityVo> getVoClazz() {
		return CityVo.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.universe.core.dao.AbstractDao#addFilters(javax.persistence.criteria
	 * .Root, javax.persistence.criteria.CriteriaBuilder,
	 * javax.persistence.criteria.Predicate, org.universe.core.vo.AbstractVo)
	 */
	@Override
	protected Predicate addFilters(Root<City> root, CriteriaBuilder builder,
	                               Predicate filters, CityVo vo) {

		return filters;
	}

	@Override
	public CountryVo findByName(EntityManager em, String name) {
		return null;
	}

	@Override
	public Boolean checkNameUniqueness(EntityManager em, String name, Long excludeId) {
		return null;
	}
}

