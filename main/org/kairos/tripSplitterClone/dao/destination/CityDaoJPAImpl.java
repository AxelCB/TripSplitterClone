package org.kairos.tripSplitterClone.dao.destination;

import org.apache.commons.lang.StringUtils;
import org.kairos.tripSplitterClone.dao.AbstractDao;
import org.kairos.tripSplitterClone.model.destination.City;
import org.kairos.tripSplitterClone.model.destination.City_;
import org.kairos.tripSplitterClone.vo.destination.CityVo;
import org.kairos.tripSplitterClone.vo.destination.CountryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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

		if (StringUtils.isNotBlank(vo.getName())) {
			filters = builder.and(
					filters,builder.like(builder.lower(root.get(City_.name)), ("%"+ vo.getName()+ "%").toLowerCase()));
		}
		return filters;
	}

	@Override
	public CityVo findByName(EntityManager em, String name) {
		this.logger.debug("getting city by name: {}", name);

		// builds the query
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<City> query = builder.createQuery(this.getClazz());
		Root<City> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();

		// filters by name
		filters = builder.and(filters, builder.like(
				builder.lower(root.get(City_.name).as(String.class)),
				name.toLowerCase()));
		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(root.get(City_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		try {
			// fetch the document type
			City city = em.createQuery(query).getSingleResult();

			return this.map(city);
		} catch (NoResultException e) {
			// there was no city with required name
			return null;
		}

	}

	@Override
	public Boolean checkNameUniqueness(EntityManager em, String name, Long excludeId) {
		this.logger.debug("searching city by name: {}, id !=: {}", name,
				excludeId);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<City> query = builder.createQuery(City.class);
		Root<City> root = query.from(this.getClazz());

		Predicate filters = builder.conjunction();

		// filters by acronym
		filters = builder.and(filters, builder.like(
				builder.lower(root.get(City_.name).as(String.class)),
				name.toLowerCase()));

		if (excludeId != null) {
			// filters for ID different than the excluded ID
			filters = builder.and(filters, builder.notEqual(root.get(City_.id)
					.as(Long.class), excludeId));
		}

		// filters by the deleted flag
		filters = builder.and(filters, builder.equal(root.get(City_.deleted)
				.as(Boolean.class), Boolean.FALSE));

		query.where(filters);

		// fetch the document types
		List<City> cities= em.createQuery(query).getResultList();

		return cities.size() == 0;

	}
}

